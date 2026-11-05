package com.claramente.feature.ar.policy

import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import org.junit.Assert.assertEquals
import org.junit.Test

class OrbitPolicyTest {

    @Test
    fun draggingAcrossTheWholeWidthTurnsTheModelPartway() {
        assertEquals(220f, OrbitPolicy.yaw(0f, dxPx = 1080f, widthPx = 1080), 1e-4f)
        assertEquals(-110f, OrbitPolicy.yaw(0f, dxPx = -540f, widthPx = 1080), 1e-4f)
        assertEquals(220f, OrbitPolicy.yawVelocity(1080f, widthPx = 1080), 1e-4f)
    }

    @Test
    fun draggingUpTiltsTheCameraAboveTheModelWithinLimits() {
        assertEquals(58f, OrbitPolicy.pitch(18f, dyPx = -500f, heightPx = 2000), 1e-4f)
        assertEquals(OrbitPolicy.MAX_PITCH_DEGREES, OrbitPolicy.pitch(70f, dyPx = -2000f, heightPx = 2000), 0f)
        assertEquals(OrbitPolicy.MIN_PITCH_DEGREES, OrbitPolicy.pitch(-70f, dyPx = 2000f, heightPx = 2000), 0f)
    }

    @Test
    fun pinchZoomStaysWithinTheAllowedRange() {
        assertEquals(2f, OrbitPolicy.zoom(1f, 2f), 1e-6f)
        assertEquals(OrbitPolicy.MAX_ZOOM, OrbitPolicy.zoom(2f, 10f), 0f)
        assertEquals(OrbitPolicy.MIN_ZOOM, OrbitPolicy.zoom(1f, 0.1f), 0f)
    }

    @Test
    fun autoSpinAdvancesAtAConstantSpeed() {
        assertEquals(24f, OrbitPolicy.spin(0f, 1_000_000_000L), 1e-4f)
        assertEquals(12f, OrbitPolicy.spin(0f, 500_000_000L), 1e-4f)
    }

    @Test
    fun restingYawIsTheNearestFullTurn() {
        assertEquals(0f, OrbitPolicy.restYaw(170f), 0f)
        assertEquals(360f, OrbitPolicy.restYaw(370f), 0f)
        assertEquals(-360f, OrbitPolicy.restYaw(-200f), 0f)
    }

    @Test
    fun widerModelsStartFartherFromTheCamera() {
        assertEquals(0.78f, OrbitPolicy.baseDistance(ArModels.SOLAR), 0f)
        assertEquals(0.6f, OrbitPolicy.baseDistance(ArModels.SOLIDOS), 0f)
        assertEquals(OrbitPolicy.DEFAULT_DISTANCE_METERS, OrbitPolicy.baseDistance(ArModels.AGUA), 0f)
        assertEquals(OrbitPolicy.DEFAULT_DISTANCE_METERS, OrbitPolicy.baseDistance(ArModel("x", "X", "", "", "file:///x.glb")), 0f)
        assertEquals(0.3f, OrbitPolicy.distance(0.6f, zoom = 2f), 1e-6f)
    }

    @Test
    fun cameraStartsInFrontOfTheModelAndOrbitsAroundIt() {
        val front = OrbitPolicy.eye(yawDegrees = 0f, pitchDegrees = 0f, distance = 0.5f)
        assertEquals(0f, front.x, 1e-5f)
        assertEquals(OrbitPolicy.target.y, front.y, 1e-5f)
        assertEquals(0.5f, front.z, 1e-5f)

        val turned = OrbitPolicy.eye(yawDegrees = 90f, pitchDegrees = 0f, distance = 0.5f)
        assertEquals(-0.5f, turned.x, 1e-5f)
        assertEquals(0f, turned.z, 1e-5f)

        val above = OrbitPolicy.eye(yawDegrees = 0f, pitchDegrees = 90f, distance = 0.5f)
        assertEquals(OrbitPolicy.target.y + 0.5f, above.y, 1e-5f)
        assertEquals(0f, above.z, 1e-5f)
    }
}
