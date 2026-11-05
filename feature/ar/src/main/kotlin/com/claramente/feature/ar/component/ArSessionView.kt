package com.claramente.feature.ar.component

import com.claramente.feature.ar.helper.ImageTargetDatabaseBuilder
import com.claramente.feature.ar.helper.OrbitTracker
import com.claramente.feature.ar.helper.QrFrameScanner
import com.claramente.feature.ar.policy.CubeFacePolicy
import com.claramente.feature.ar.policy.PlacementReadinessPolicy
import com.claramente.feature.ar.policy.QrLayoutPolicy
import com.claramente.feature.ar.policy.QrPlacementPolicy
import com.claramente.feature.ar.policy.TrackingGracePolicy
import com.claramente.feature.ar.policy.TrackingHintPolicy
import com.claramente.feature.ar.state.ArExperienceActions
import com.claramente.feature.ar.state.ArExperienceUiState
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ImageTargets
import com.claramente.feature.ar.state.PlacedQrModel
import com.claramente.feature.ar.state.QrPlacement
import android.os.SystemClock
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
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
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import com.google.ar.core.Pose
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
internal fun ArSessionView(state: ArExperienceUiState, actions: ArExperienceActions, tracker: OrbitTracker) {
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
    val placements = remember { mutableStateListOf<PlacedQrModel>() }
    var pending by remember { mutableStateOf<QrPlacement?>(null) }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    var steadyFrames by remember { mutableIntStateOf(0) }
    val scanner = remember { QrFrameScanner() }
    val cameraPosition: () -> Position = remember(cameraNode) { { cameraNode.worldPosition } }
    val needsRescan by remember { derivedStateOf { currentState.model == null } }
    val imageMethods = remember {
        setOf(AugmentedImage.TrackingMethod.FULL_TRACKING, AugmentedImage.TrackingMethod.LAST_KNOWN_POSE)
    }

    DisposableEffect(Unit) {
        onDispose {
            scanner.close()
            placements.forEach { it.anchor.detach() }
        }
    }

    LaunchedEffect(needsRescan) {
        if (mode == ArMode.QR && needsRescan) {
            placements.forEach { it.anchor.detach() }
            placements.clear()
            pending = null
        }
    }

    fun qrViewPoint(frame: Frame, request: QrPlacement): Pair<Float, Float>? {
        if (viewSize.width <= 0) return null
        val x = request.code.imageX
        val y = request.code.imageY
        if (x == null || y == null) return viewSize.width / 2f to viewSize.height / 2f
        val view = FloatArray(2)
        return runCatching {
            frame.transformCoordinates2d(Coordinates2d.IMAGE_PIXELS, floatArrayOf(x, y), Coordinates2d.VIEW, view)
            view[0] to view[1]
        }.getOrNull()?.takeIf { (vx, vy) -> vx in 0f..viewSize.width.toFloat() && vy in 0f..viewSize.height.toFloat() }
            ?: (viewSize.width / 2f to viewSize.height / 2f)
    }

    fun placeQr(session: Session, frame: Frame, nowMs: Long) {
        val request = pending ?: return
        if (frame.camera.trackingState != TrackingState.TRACKING || !PlacementReadinessPolicy.isReady(steadyFrames)) return
        val viewPoint = qrViewPoint(frame, request) ?: return
        val hit = QrPlacementPolicy.placementHit(frame, viewPoint.first, viewPoint.second)
        val created = when {
            hit != null -> QrPlacementPolicy.anchor(hit)
            QrPlacementPolicy.shouldFloat(request.requestedAtMs, nowMs) -> {
                val cameraPose = frame.camera.displayOrientedPose
                val translation = QrLayoutPolicy.spaced(
                    candidate = QrPlacementPolicy.floatingPose(cameraPose).translation,
                    occupied = placements.map { it.anchor.pose.translation },
                    step = QrLayoutPolicy.sideStep(cameraPose.xAxis),
                )
                runCatching { session.createAnchor(Pose(translation, floatArrayOf(0f, 0f, 0f, 1f))) }.getOrNull()
            }
            else -> null
        }
        val placed = created ?: return
        placements.add(PlacedQrModel(placed, request.model, onSurface = QrPlacementPolicy.restsOnSurface(hit)))
        pending = null
        currentActions.onQr(request.code.raw)
    }

    fun trackImages(frame: Frame, nowMs: Long) {
        frame.getUpdatedTrackables(AugmentedImage::class.java).forEach { image ->
            if (image.trackingState == TrackingState.TRACKING &&
                image.trackingMethod == AugmentedImage.TrackingMethod.FULL_TRACKING &&
                tracked.none { it.index == image.index }
            ) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        ARSceneView(
            modifier = Modifier.fillMaxSize().onSizeChanged { viewSize = it },
            engine = engine,
            cameraNode = cameraNode,
            planeFindingMode = if (mode == ArMode.QR) {
                Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
            } else {
                Config.PlaneFindingMode.DISABLED
            },
            depthMode = if (mode == ArMode.QR) Config.DepthMode.AUTOMATIC else Config.DepthMode.DISABLED,
            instantPlacementMode = if (mode == ArMode.QR) {
                Config.InstantPlacementMode.LOCAL_Y_UP
            } else {
                Config.InstantPlacementMode.DISABLED
            },
            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE,
            focusMode = Config.FocusMode.AUTO,
            sessionConfiguration = { _, config ->
                config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            },
            planeRenderer = false,
            onGestureListener = null,
            permissionHandler = null,
            cameraPermissionOverlay = null,
            arCoreAvailabilityOverlay = null,
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
            onTrackingFailureChanged = { reason -> currentActions.onTrackingHint(TrackingHintPolicy.hint(reason)) },
            onSessionUpdated = { session, frame ->
                val nowMs = SystemClock.elapsedRealtime()
                steadyFrames = PlacementReadinessPolicy.steadyFrames(
                    previous = steadyFrames,
                    tracking = frame.camera.trackingState == TrackingState.TRACKING,
                )
                if (mode == ArMode.QR) {
                    if (pending != null) {
                        placeQr(session, frame, nowMs)
                    } else if (placements.size < QrLayoutPolicy.MAX_MODELS) {
                        scanner.scan(frame, nowMs) { codes ->
                            val placedIds = placements.map { it.model.id }
                            val known = codes.mapNotNull { code -> currentState.resolveQr(code.raw)?.let { code to it } }
                            val next = known.firstOrNull { (_, model) -> QrLayoutPolicy.canAdd(placedIds, model.id) }
                            when {
                                next != null && pending == null ->
                                    pending = QrPlacement(next.first, next.second, SystemClock.elapsedRealtime())
                                known.isEmpty() -> currentActions.onQr(codes.first().raw)
                            }
                        }
                    }
                } else {
                    trackImages(frame, nowMs)
                }
            },
        ) {
            val model = state.model
            if (mode == ArMode.QR) {
                val qrScale = if (placements.size > 1) ArSceneDefaults.MULTI_QR_SCALE else ArSceneDefaults.SINGLE_QR_SCALE
                placements.forEach { placed ->
                    key(placed.anchor) {
                        AnchorNode(
                            anchor = placed.anchor,
                            apply = {
                                isSmoothTransformEnabled = true
                                smoothTransformSpeed = ArSceneDefaults.POSE_SMOOTHING_SPEED
                            },
                        ) {
                            Node(
                                position = Position(y = if (placed.onSurface) ArSceneDefaults.SURFACE_LIFT_METERS else 0f),
                                scale = Scale(qrScale),
                            ) {
                                SpinnableModel(placed.model, tracker, cameraPosition)
                            }
                        }
                    }
                }
            } else if (model != null) {
                tracked.forEach { image ->
                    key(image.index) {
                        val shown = if (mode == ArMode.CUBE) image.index == activeFace else image.index in visibleImages
                        AugmentedImageNode(
                            augmentedImage = image,
                            visibleTrackingMethods = imageMethods,
                            apply = {
                                isSmoothTransformEnabled = true
                                smoothTransformSpeed = ArSceneDefaults.POSE_SMOOTHING_SPEED
                            },
                        ) {
                            if (mode == ArMode.CUBE) {
                                Node(
                                    position = Position(y = ArSceneDefaults.CUBE_CENTER_OFFSET_METERS),
                                    scale = Scale(ArSceneDefaults.CUBE_SCALE),
                                    isVisible = shown,
                                ) {
                                    SpinnableModel(model, tracker, cameraPosition)
                                }
                            } else {
                                Node(position = Position(y = ArSceneDefaults.MARKER_LIFT_METERS), isVisible = shown) {
                                    SpinnableModel(model, tracker, cameraPosition)
                                }
                            }
                        }
                    }
                }
            }
        }
        OrbitGestureSurface(tracker)
    }
}
