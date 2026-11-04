package com.claramente.feature.ar.component

import io.github.sceneview.SceneScope
import io.github.sceneview.math.Position
import io.github.sceneview.rememberModelInstance
import androidx.compose.runtime.Composable

@Composable
internal fun SceneScope.GlbModelScene(fileUri: String) {
    val instance = rememberModelInstance(modelLoader, fileLocation = fileUri) ?: return
    ModelNode(
        modelInstance = instance,
        scaleToUnits = GlbSceneDefaults.SIZE_METERS,
        centerOrigin = Position(x = 0f, y = -1f, z = 0f),
    )
}
