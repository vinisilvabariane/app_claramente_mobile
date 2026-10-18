package com.claramente.feature.ar.viewmodel

import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModels
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
}
