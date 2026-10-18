package com.claramente.feature.ar.policy

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QrLayoutPolicyTest {

    @Test
    fun acceptsNewModelsUpToTheLimit() {
        assertTrue(QrLayoutPolicy.canAdd(emptyList(), "solar"))
        assertTrue(QrLayoutPolicy.canAdd(listOf("solar", "agua", "solidos"), "custom"))
        assertFalse(QrLayoutPolicy.canAdd(listOf("solar", "agua", "solidos", "custom"), "outro"))
    }

    @Test
    fun rejectsAModelThatIsAlreadyPlaced() {
        assertFalse(QrLayoutPolicy.canAdd(listOf("solar"), "solar"))
    }

    @Test
    fun keepsTheCandidateWhenThereIsRoom() {
        val candidate = floatArrayOf(0f, 0f, -0.5f)
        val result = QrLayoutPolicy.spaced(candidate, listOf(floatArrayOf(1f, 0f, -0.5f)), floatArrayOf(0.22f, 0f, 0f))
        assertArrayEquals(candidate, result, 0f)
    }

    @Test
    fun shiftsSidewaysUntilItNoLongerOverlaps() {
        val step = QrLayoutPolicy.sideStep(floatArrayOf(1f, 0.3f, 0f))
        val occupied = listOf(floatArrayOf(0f, 0f, -0.5f), floatArrayOf(0.22f, 0f, -0.5f))
        val result = QrLayoutPolicy.spaced(floatArrayOf(0.05f, 0f, -0.5f), occupied, step)
        assertArrayEquals(floatArrayOf(0.49f, 0f, -0.5f), result, 1e-5f)
    }

    @Test
    fun sideStepStaysHorizontal() {
        val step = QrLayoutPolicy.sideStep(floatArrayOf(0f, 0.6f, 0.8f))
        assertArrayEquals(floatArrayOf(0f, 0f, 0.22f), step, 1e-5f)
    }
}
