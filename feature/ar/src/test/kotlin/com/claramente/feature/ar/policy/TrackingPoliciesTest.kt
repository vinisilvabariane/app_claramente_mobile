package com.claramente.feature.ar.policy

import com.google.ar.core.TrackingFailureReason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackingPoliciesTest {

    @Test
    fun modelStaysVisibleDuringShortTrackingLoss() {
        assertTrue(TrackingGracePolicy.isVisible(lastSeenMs = 1_000L, nowMs = 1_000L))
        assertTrue(TrackingGracePolicy.isVisible(lastSeenMs = 1_000L, nowMs = 2_100L))
    }

    @Test
    fun modelHidesAfterGracePeriod() {
        assertFalse(TrackingGracePolicy.isVisible(lastSeenMs = 1_000L, nowMs = 2_300L))
        assertFalse(TrackingGracePolicy.isVisible(lastSeenMs = null, nowMs = 2_300L))
    }

    @Test
    fun cubeKeepsCurrentFaceWhileItIsVisible() {
        assertEquals(3, CubeFacePolicy.select(setOf(0, 3, 5), current = 3))
    }

    @Test
    fun cubeSwitchesToAnotherVisibleFaceWhenCurrentIsLost() {
        assertEquals(0, CubeFacePolicy.select(setOf(0, 5), current = 3))
        assertNull(CubeFacePolicy.select(emptySet(), current = 3))
    }

    @Test
    fun placementWaitsForSteadyTrackingAndRestartsAfterALoss() {
        var frames = 0
        repeat(11) { frames = PlacementReadinessPolicy.steadyFrames(frames, tracking = true) }
        assertFalse(PlacementReadinessPolicy.isReady(frames))
        frames = PlacementReadinessPolicy.steadyFrames(frames, tracking = true)
        assertTrue(PlacementReadinessPolicy.isReady(frames))
        frames = PlacementReadinessPolicy.steadyFrames(frames, tracking = true)
        assertTrue(PlacementReadinessPolicy.isReady(frames))
        frames = PlacementReadinessPolicy.steadyFrames(frames, tracking = false)
        assertEquals(0, frames)
        assertFalse(PlacementReadinessPolicy.isReady(frames))
    }

    @Test
    fun trackingFailuresBecomeHintsForTheStudent() {
        assertNull(TrackingHintPolicy.hint(null))
        assertNull(TrackingHintPolicy.hint(TrackingFailureReason.NONE))
        assertEquals("Mova o celular mais devagar", TrackingHintPolicy.hint(TrackingFailureReason.EXCESSIVE_MOTION))
        assertEquals("Pouca luz: vá para um lugar mais claro", TrackingHintPolicy.hint(TrackingFailureReason.INSUFFICIENT_LIGHT))
        assertEquals(
            "Aponte para uma superfície com mais detalhes",
            TrackingHintPolicy.hint(TrackingFailureReason.INSUFFICIENT_FEATURES),
        )
    }
}
