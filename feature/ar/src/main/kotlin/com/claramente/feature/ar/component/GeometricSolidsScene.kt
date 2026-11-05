package com.claramente.feature.ar.component

import io.github.sceneview.SceneScope
import io.github.sceneview.math.Position
import io.github.sceneview.math.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal fun SceneScope.GeometricSolidsScene(cameraPosition: () -> Position) {
    val cube = remember(materialLoader) { materialLoader.createColorInstance(0xFF7C5CFF.toInt(), 0f, 0.5f, 0.3f) }
    val sphere = remember(materialLoader) { materialLoader.createColorInstance(0xFF46C26E.toInt(), 0f, 0.5f, 0.3f) }
    val pyramid = remember(materialLoader) { materialLoader.createColorInstance(0xFFFF9224.toInt(), 0f, 0.5f, 0.3f) }
    Node(position = Position(x = -0.07f)) {
        CubeNode(size = Size(0.045f, 0.045f, 0.045f), materialInstance = cube)
        SceneLabel("Cubo", Position(y = 0.05f), cameraPosition)
    }
    Node {
        SphereNode(radius = 0.024f, materialInstance = sphere)
        SceneLabel("Esfera", Position(y = 0.05f), cameraPosition)
    }
    Node(position = Position(x = 0.07f)) {
        ConeNode(radius = 0.03f, height = 0.05f, sideCount = 4, materialInstance = pyramid)
        SceneLabel("Pirâmide", Position(y = 0.05f), cameraPosition)
    }
}
