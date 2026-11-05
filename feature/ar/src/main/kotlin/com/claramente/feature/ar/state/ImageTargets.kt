package com.claramente.feature.ar.state

object ImageTargets {
    private const val MARKER_WIDTH_METERS = 0.12f
    private const val CUBE_FACE_WIDTH_METERS = 0.05f
    private const val CUBE_FACES = 6

    val marker = ImageTarget("flat-marker", "ar/marker.jpg", MARKER_WIDTH_METERS)

    val cubeFaces: List<ImageTarget> = (1..CUBE_FACES).map { face ->
        ImageTarget("cube-face-$face", "ar/cube-face-$face.jpg", CUBE_FACE_WIDTH_METERS)
    }

    fun forMode(mode: ArMode): List<ImageTarget> = when (mode) {
        ArMode.MARKER -> listOf(marker)
        ArMode.CUBE -> cubeFaces
        ArMode.QR, ArMode.VIEWER -> emptyList()
    }
}
