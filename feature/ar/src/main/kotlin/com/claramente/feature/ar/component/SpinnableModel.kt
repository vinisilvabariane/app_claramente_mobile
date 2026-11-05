package com.claramente.feature.ar.component

import com.claramente.feature.ar.helper.OrbitTracker
import com.claramente.feature.ar.state.ArModel
import io.github.sceneview.SceneScope
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import androidx.compose.runtime.Composable

@Composable
internal fun SceneScope.SpinnableModel(model: ArModel, tracker: OrbitTracker, cameraPosition: () -> Position) {
    Node(rotation = Rotation(y = tracker.yaw), scale = Scale(tracker.zoom)) {
        ModelScene(model, cameraPosition)
    }
}
