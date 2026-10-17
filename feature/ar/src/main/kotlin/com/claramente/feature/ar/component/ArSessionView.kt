package com.claramente.feature.ar.component

import com.claramente.feature.ar.helper.ImageTargetDatabaseBuilder
import com.claramente.feature.ar.helper.QrFrameScanner
import com.claramente.feature.ar.policy.CubeFacePolicy
import com.claramente.feature.ar.policy.QrPlacementPolicy
import com.claramente.feature.ar.policy.TrackingGracePolicy
import com.claramente.feature.ar.state.ArExperienceActions
import com.claramente.feature.ar.state.ArExperienceUiState
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ImageTargets
import com.claramente.feature.ar.state.QrPlacement
import android.os.SystemClock
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import com.google.ar.core.Anchor
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.rememberEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun ArSessionView(state: ArExperienceUiState, actions: ArExperienceActions) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine)
    val currentState by rememberUpdatedState(state)
    val currentActions by rememberUpdatedState(actions)
    val mode = state.mode
    val tracked = remember { mutableStateListOf<AugmentedImage>() }
    val lastSeen = remember { HashMap<Int, Long>() }
    var visibleImages by remember { mutableStateOf(emptySet<Int>()) }
    var activeFace by remember { mutableStateOf<Int?>(null) }
    var anchor by remember { mutableStateOf<Anchor?>(null) }
    var onSurface by remember { mutableStateOf(false) }
    var pending by remember { mutableStateOf<QrPlacement?>(null) }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    val scanner = remember { QrFrameScanner() }
    val cameraPosition: () -> Position = remember(cameraNode) { { cameraNode.worldPosition } }
    val needsRescan by remember { derivedStateOf { currentState.model == null } }
    val steadyStates = remember { setOf(TrackingState.TRACKING, TrackingState.PAUSED) }
    val imageMethods = remember {
        setOf(AugmentedImage.TrackingMethod.FULL_TRACKING, AugmentedImage.TrackingMethod.LAST_KNOWN_POSE)
    }

    DisposableEffect(Unit) {
        onDispose {
            scanner.close()
            anchor?.detach()
        }
    }

    LaunchedEffect(needsRescan) {
        if (mode == ArMode.QR && needsRescan) {
            anchor?.detach()
            anchor = null
            pending = null
            onSurface = false
        }
    }

    fun placeQr(session: Session, frame: Frame, nowMs: Long) {
        val request = pending ?: return
        if (frame.camera.trackingState != TrackingState.TRACKING) return
        val hit = if (viewSize.width > 0) {
            QrPlacementPolicy.surfaceHit(frame, viewSize.width / 2f, viewSize.height / 2f)
        } else {
            null
        }
        val placed = when {
            hit != null -> runCatching { hit.createAnchor() }.getOrNull()?.also { onSurface = true }
            QrPlacementPolicy.shouldFloat(request.requestedAtMs, nowMs) ->
                runCatching { session.createAnchor(QrPlacementPolicy.floatingPose(frame.camera.displayOrientedPose)) }
                    .getOrNull()
                    ?.also { onSurface = false }
            else -> null
        }
        if (placed != null) {
            anchor = placed
            pending = null
            currentActions.onQr(request.raw)
        }
    }

    fun trackImages(frame: Frame, nowMs: Long) {
        frame.getUpdatedTrackables(AugmentedImage::class.java).forEach { image ->
            if (image.trackingState == TrackingState.TRACKING && tracked.none { it.index == image.index }) {
                tracked.add(image)
            }
        }
        tracked.forEach { image ->
            if (image.trackingState == TrackingState.TRACKING &&
                image.trackingMethod == AugmentedImage.TrackingMethod.FULL_TRACKING
            ) {
                lastSeen[image.index] = nowMs
            }
        }
        val visible = tracked
            .filter { it.trackingState != TrackingState.STOPPED && TrackingGracePolicy.isVisible(lastSeen[it.index], nowMs) }
            .map { it.index }
            .toSet()
        if (visible != visibleImages) visibleImages = visible
        val found = visible.isNotEmpty()
        if (found != currentState.found) currentActions.onFound(found)
        if (mode == ArMode.CUBE) {
            val next = CubeFacePolicy.select(visible, activeFace)
            if (next != activeFace) activeFace = next
        }
    }

    ARSceneView(
        modifier = Modifier.fillMaxSize().onSizeChanged { viewSize = it },
        engine = engine,
        cameraNode = cameraNode,
        planeRenderer = false,
        permissionHandler = null,
        sessionConfiguration = { _, config ->
            config.focusMode = Config.FocusMode.AUTO
            config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            config.planeFindingMode = if (mode == ArMode.QR) {
                Config.PlaneFindingMode.HORIZONTAL
            } else {
                Config.PlaneFindingMode.DISABLED
            }
        },
        onSessionCreated = { session ->
            val targets = ImageTargets.forMode(mode)
            if (targets.isNotEmpty()) {
                scope.launch {
                    val database = withContext(Dispatchers.Default) {
                        runCatching { ImageTargetDatabaseBuilder.build(context, session, targets) }.getOrNull()
                    }
                    if (database != null) {
                        runCatching {
                            val config = session.config
                            config.augmentedImageDatabase = database
                            session.configure(config)
                        }
                    }
                }
            }
        },
        onSessionFailed = { error -> currentActions.onSessionFailed(error.message) },
        onSessionUpdated = { session, frame ->
            val nowMs = SystemClock.elapsedRealtime()
            if (mode == ArMode.QR) {
                if (anchor == null && pending != null) {
                    placeQr(session, frame, nowMs)
                } else if (anchor == null && currentState.model == null) {
                    scanner.scan(frame, nowMs) { raw ->
                        if (currentState.resolveQr(raw) == null) {
                            currentActions.onQr(raw)
                        } else if (anchor == null && pending == null) {
                            pending = QrPlacement(raw, SystemClock.elapsedRealtime())
                        }
                    }
                }
            } else {
                trackImages(frame, nowMs)
            }
        },
    ) {
        val model = state.model
        if (model != null) {
            when (mode) {
                ArMode.MARKER, ArMode.CUBE -> tracked.forEach { image ->
                    key(image.index) {
                        val shown = if (mode == ArMode.CUBE) image.index == activeFace else image.index in visibleImages
                        AugmentedImageNode(
                            augmentedImage = image,
                            visibleTrackingMethods = imageMethods,
                            apply = { visibleCameraTrackingStates = steadyStates },
                        ) {
                            if (mode == ArMode.CUBE) {
                                Node(position = Position(y = -0.025f), scale = Scale(0.45f), isVisible = shown) {
                                    ModelScene(model, cameraPosition)
                                }
                            } else {
                                Node(position = Position(y = 0.03f), isVisible = shown) {
                                    ModelScene(model, cameraPosition)
                                }
                            }
                        }
                    }
                }
                ArMode.QR -> anchor?.let { placed ->
                    key(placed) {
                        AnchorNode(
                            anchor = placed,
                            visibleTrackingStates = steadyStates,
                            apply = { visibleCameraTrackingStates = steadyStates },
                        ) {
                            Node(position = Position(y = if (onSurface) 0.06f else 0f), scale = Scale(1.5f)) {
                                ModelScene(model, cameraPosition)
                            }
                        }
                    }
                }
            }
        }
    }
}
