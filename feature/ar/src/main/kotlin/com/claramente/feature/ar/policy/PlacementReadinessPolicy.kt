package com.claramente.feature.ar.policy

internal object PlacementReadinessPolicy {
    private const val MIN_TRACKING_FRAMES = 12

    fun steadyFrames(previous: Int, tracking: Boolean): Int =
        if (tracking) minOf(previous + 1, MIN_TRACKING_FRAMES) else 0

    fun isReady(steadyFrames: Int): Boolean = steadyFrames >= MIN_TRACKING_FRAMES
}
