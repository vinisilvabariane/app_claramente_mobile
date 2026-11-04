package com.claramente.feature.ar.component

import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import io.github.sceneview.SceneScope
import io.github.sceneview.math.Position
import androidx.compose.runtime.Composable

@Composable
internal fun SceneScope.ModelScene(model: ArModel, cameraPosition: () -> Position) {
    val fileUri = model.fileUri
    if (fileUri != null) {
        GlbModelScene(fileUri)
        return
    }
    when (model.id) {
        ArModels.SOLAR.id -> SolarSystemScene(cameraPosition)
        ArModels.AGUA.id -> WaterMoleculeScene(cameraPosition)
        else -> GeometricSolidsScene(cameraPosition)
    }
}
