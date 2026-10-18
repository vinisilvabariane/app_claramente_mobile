package com.claramente.feature.ar.policy

import kotlin.math.sqrt

internal object QrLayoutPolicy {
    const val MAX_MODELS = 4
    private const val MIN_SPACING_METERS = 0.22f

    fun canAdd(placedIds: List<String>, id: String): Boolean = placedIds.size < MAX_MODELS && id !in placedIds

    fun sideStep(cameraXAxis: FloatArray): FloatArray {
        val length = sqrt(cameraXAxis[0] * cameraXAxis[0] + cameraXAxis[2] * cameraXAxis[2])
        if (length < 1e-4f) return floatArrayOf(MIN_SPACING_METERS, 0f, 0f)
        return floatArrayOf(cameraXAxis[0] / length * MIN_SPACING_METERS, 0f, cameraXAxis[2] / length * MIN_SPACING_METERS)
    }

    fun spaced(candidate: FloatArray, occupied: List<FloatArray>, step: FloatArray): FloatArray {
        var position = candidate
        repeat(MAX_MODELS) {
            if (occupied.none { distance(it, position) < MIN_SPACING_METERS }) return position
            position = floatArrayOf(position[0] + step[0], position[1] + step[1], position[2] + step[2])
        }
        return position
    }

    private fun distance(a: FloatArray, b: FloatArray): Float {
        val dx = a[0] - b[0]
        val dz = a[2] - b[2]
        return sqrt(dx * dx + dz * dz)
    }
}
