package com.claramente.feature.ar.policy

import com.google.ar.core.Anchor
import com.google.ar.core.DepthPoint
import com.google.ar.core.Frame
import com.google.ar.core.HitResult
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState

internal object QrPlacementPolicy {
    private const val SURFACE_WAIT_MS = 4000L
    private const val MAX_SURFACE_DISTANCE_METERS = 4f
    private const val INSTANT_DISTANCE_METERS = 0.6f
    private val upright = floatArrayOf(0f, 0f, 0f, 1f)

    fun placementHit(frame: Frame, x: Float, y: Float): HitResult? {
        val hits = runCatching { frame.hitTest(x, y) }.getOrDefault(emptyList())
        return hits.firstOrNull { isSurface(it) || isDepth(it) }
            ?: runCatching { frame.hitTestInstantPlacement(x, y, INSTANT_DISTANCE_METERS) }
                .getOrDefault(emptyList())
                .firstOrNull { it.trackable is InstantPlacementPoint }
    }

    fun anchor(hit: HitResult): Anchor? {
        val pose = Pose(hit.hitPose.translation, upright)
        return runCatching { hit.trackable.createAnchor(pose) }.getOrNull()
            ?: runCatching { hit.createAnchor() }.getOrNull()
    }

    fun restsOnSurface(hit: HitResult?): Boolean =
        (hit?.trackable as? Plane)?.type == Plane.Type.HORIZONTAL_UPWARD_FACING

    fun shouldFloat(requestedAtMs: Long, nowMs: Long): Boolean = nowMs - requestedAtMs >= SURFACE_WAIT_MS

    fun floatingPose(cameraPose: Pose): Pose {
        val ahead = cameraPose.compose(Pose.makeTranslation(0f, -0.1f, -0.5f))
        return Pose(ahead.translation, upright)
    }

    private fun isSurface(hit: HitResult): Boolean {
        val plane = hit.trackable as? Plane ?: return false
        return plane.trackingState == TrackingState.TRACKING &&
            plane.isPoseInPolygon(hit.hitPose) &&
            hit.distance <= MAX_SURFACE_DISTANCE_METERS
    }

    private fun isDepth(hit: HitResult): Boolean =
        hit.trackable is DepthPoint && hit.distance <= MAX_SURFACE_DISTANCE_METERS
}
