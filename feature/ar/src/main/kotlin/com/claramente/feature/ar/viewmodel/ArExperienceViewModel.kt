package com.claramente.feature.ar.viewmodel

import com.claramente.feature.ar.state.ArExperienceUiState
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import com.claramente.feature.ar.state.ArSupport
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArExperienceViewModel(arWebUrl: String?) : ViewModel() {
    private val webUrl = arWebUrl?.takeIf { it.isNotBlank() }

    private val _state = MutableStateFlow(ArExperienceUiState(arWebUrl = webUrl))
    val state: StateFlow<ArExperienceUiState> = _state.asStateFlow()

    fun start(mode: ArMode, model: ArModel, available: List<ArModel> = ArModels.all) {
        _state.value = ArExperienceUiState(
            mode = mode,
            model = if (mode == ArMode.QR) null else model,
            arWebUrl = webUrl,
            available = available,
        )
    }

    fun onSupport(support: ArSupport) {
        _state.update { if (it.support == support) it else it.copy(support = support) }
    }

    fun onCameraState(granted: Boolean) {
        _state.update { if (it.cameraGranted == granted) it else it.copy(cameraGranted = granted) }
    }

    fun onCameraRequested() {
        _state.update { it.copy(cameraRequested = true) }
    }

    fun onFound(found: Boolean) {
        _state.update { if (it.found == found) it else it.copy(found = found) }
    }

    fun onQr(raw: String) {
        val resolved = _state.value.resolveQr(raw)
        _state.update {
            if (resolved != null) it.copy(model = resolved, unknownCode = null) else it.copy(unknownCode = raw)
        }
    }

    fun onRescan() {
        _state.update { it.copy(model = null, unknownCode = null) }
    }

    fun onSessionFailed(message: String?) {
        _state.update { it.copy(failure = message ?: "Falha desconhecida ao iniciar a câmera de AR.") }
    }

    fun onRetry() {
        _state.update { it.copy(failure = null, support = ArSupport.CHECKING, cameraGranted = null, attempt = it.attempt + 1) }
    }
}
