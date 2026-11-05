package com.claramente.feature.ar.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.helper.OrbitTracker
import com.claramente.feature.ar.policy.OrbitPolicy
import com.claramente.feature.ar.state.ArModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import io.github.sceneview.SceneView
import io.github.sceneview.SurfaceType
import io.github.sceneview.math.Position
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine

@Composable
internal fun ModelViewerView(model: ArModel, tracker: OrbitTracker) {
    val engine = rememberEngine()
    val cameraNode = rememberCameraNode(engine)
    val cameraPosition: () -> Position = remember(cameraNode) { { cameraNode.worldPosition } }

    LaunchedEffect(cameraNode, tracker, model.id) {
        val baseDistance = OrbitPolicy.baseDistance(model)
        snapshotFlow {
            OrbitPolicy.eye(tracker.yaw, tracker.pitch, OrbitPolicy.distance(baseDistance, tracker.zoom))
        }.collect { eye ->
            cameraNode.worldPosition = eye
            cameraNode.lookAt(OrbitPolicy.target)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ClaramenteColors.Background)) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            surfaceType = SurfaceType.TextureSurface,
            engine = engine,
            isOpaque = false,
            autoCenterContent = false,
            cameraNode = cameraNode,
            cameraManipulator = null,
            onGestureListener = null,
        ) {
            ModelScene(model, cameraPosition)
        }
        OrbitGestureSurface(tracker)
    }
}
