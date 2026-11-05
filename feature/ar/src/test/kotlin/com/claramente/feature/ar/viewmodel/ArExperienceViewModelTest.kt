package com.claramente.feature.ar.viewmodel

import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModels
import com.claramente.feature.ar.state.ArStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArExperienceViewModelTest {

    private fun qrSession() = ArExperienceViewModel(arWebUrl = null).apply { start(ArMode.QR, ArModels.SOLAR) }

    @Test
    fun eachNewQrAddsAModel() {
        val viewModel = qrSession()
        viewModel.onQr("claramente:solar")
        viewModel.onQr("claramente:agua")
        assertEquals(listOf(ArModels.SOLAR, ArModels.AGUA), viewModel.state.value.qrModels)
        assertEquals(ArModels.AGUA, viewModel.state.value.model)
        assertEquals("Sistema Solar + Molécula de água", viewModel.state.value.title)
    }

    @Test
    fun readingTheSameQrAgainDoesNotDuplicate() {
        val viewModel = qrSession()
        viewModel.onQr("claramente:solar")
        viewModel.onQr("claramente:solar")
        assertEquals(listOf(ArModels.SOLAR), viewModel.state.value.qrModels)
    }

    @Test
    fun unknownQrKeepsPlacedModels() {
        val viewModel = qrSession()
        viewModel.onQr("claramente:solar")
        viewModel.onQr("https://outro-site")
        assertEquals(listOf(ArModels.SOLAR), viewModel.state.value.qrModels)
        assertEquals("https://outro-site", viewModel.state.value.unknownCode)
    }

    @Test
    fun clearRemovesEveryModel() {
        val viewModel = qrSession()
        viewModel.onQr("claramente:solar")
        viewModel.onQr("claramente:agua")
        viewModel.onRescan()
        assertEquals(emptyList<Any>(), viewModel.state.value.qrModels)
        assertNull(viewModel.state.value.model)
    }

    @Test
    fun viewerRunsWithoutCameraOrArCore() {
        val viewModel = ArExperienceViewModel(arWebUrl = null).apply { start(ArMode.VIEWER, ArModels.AGUA) }
        assertEquals(ArStage.RUNNING, viewModel.state.value.stage)
        assertEquals(ArModels.AGUA, viewModel.state.value.model)
        assertEquals("Molécula de água", viewModel.state.value.title)
    }

    @Test
    fun switchingToTheViewerKeepsTheModelAndClearsTheFailure() {
        val viewModel = ArExperienceViewModel(arWebUrl = null).apply { start(ArMode.MARKER, ArModels.SOLIDOS) }
        viewModel.onSessionFailed("sem câmera")
        assertEquals(ArStage.FAILED, viewModel.state.value.stage)
        viewModel.onViewer()
        assertEquals(ArMode.VIEWER, viewModel.state.value.mode)
        assertEquals(ArModels.SOLIDOS, viewModel.state.value.model)
        assertNull(viewModel.state.value.failure)
        assertEquals(ArStage.RUNNING, viewModel.state.value.stage)
    }

    @Test
    fun switchingToTheViewerFromQrPicksTheFirstAvailableModel() {
        val viewModel = ArExperienceViewModel(arWebUrl = null)
        viewModel.start(ArMode.QR, ArModels.SOLAR, available = listOf(ArModels.AGUA, ArModels.SOLAR))
        viewModel.onViewer()
        assertEquals(ArModels.AGUA, viewModel.state.value.model)
    }

    @Test
    fun trackingHintsAreKeptUntilTrackingRecovers() {
        val viewModel = ArExperienceViewModel(arWebUrl = null).apply { start(ArMode.MARKER, ArModels.SOLAR) }
        viewModel.onTrackingHint("Mova o celular mais devagar")
        assertEquals("Mova o celular mais devagar", viewModel.state.value.trackingHint)
        viewModel.onTrackingHint(null)
        assertNull(viewModel.state.value.trackingHint)
    }
}
