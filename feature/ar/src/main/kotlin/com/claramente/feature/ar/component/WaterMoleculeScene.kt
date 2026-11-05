package com.claramente.feature.ar.component

import io.github.sceneview.SceneScope
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal fun SceneScope.WaterMoleculeScene(cameraPosition: () -> Position) {
    val oxygen = remember(materialLoader) { materialLoader.createColorInstance(0xFFE0453B.toInt(), 0f, 0.5f, 0.3f) }
    val hydrogen = remember(materialLoader) { materialLoader.createColorInstance(0xFFF2F5F7.toInt(), 0f, 0.5f, 0.3f) }
    val bond = remember(materialLoader) { materialLoader.createColorInstance(0xFF9AA7AF.toInt(), 0f, 0.8f, 0f) }
    SphereNode(radius = 0.026f, materialInstance = oxygen)
    SceneLabel("O", Position(y = 0.05f), cameraPosition)
    listOf(52.25f, -52.25f).forEach { angle ->
        Node(rotation = Rotation(z = angle)) {
            CubeNode(
                size = Size(0.006f, 0.05f, 0.006f),
                materialInstance = bond,
                position = Position(y = 0.05f / 2f),
            )
            Node(position = Position(y = 0.05f)) {
                SphereNode(radius = 0.014f, materialInstance = hydrogen)
                SceneLabel("H", Position(y = 0.03f), cameraPosition)
            }
        }
    }
}
