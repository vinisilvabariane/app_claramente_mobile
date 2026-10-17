package com.claramente.feature.teacher.viewmodel

import com.claramente.core.domain.contract.IDeleteModelUseCase
import com.claramente.core.domain.contract.IInspectModelFileUseCase
import com.claramente.core.domain.contract.IListLibraryUseCase
import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.domain.contract.IUploadModelUseCase
import com.claramente.core.domain.policy.ErrorMessagePolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.feature.teacher.state.TeacherLibraryUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherLibraryViewModel(
    private val listLibrary: IListLibraryUseCase,
    private val inspectModelFile: IInspectModelFileUseCase,
    private val uploadModel: IUploadModelUseCase,
    private val deleteModel: IDeleteModelUseCase,
    private val prepareModel: IPrepareModelUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TeacherLibraryUiState())
    val state: StateFlow<TeacherLibraryUiState> = _state.asStateFlow()

    fun load() {
        val draft = _state.value
        _state.value = TeacherLibraryUiState(
            loading = true,
            file = draft.file,
            title = draft.title,
            subject = draft.subject,
            description = draft.description,
        )
        refresh()
    }

    fun clear() {
        _state.value = TeacherLibraryUiState()
    }

    fun pickFile(uri: String) {
        _state.update { it.copy(inspecting = true, error = null, notice = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { inspectModelFile.execute(uri) }
            result.fold(
                onSuccess = { info ->
                    _state.update {
                        it.copy(
                            inspecting = false,
                            file = info,
                            title = it.title.ifBlank { info.name.substringBeforeLast('.').replace('_', ' ').trim() },
                        )
                    }
                },
                onFailure = { error -> fail(error) { it.copy(inspecting = false, file = null) } },
            )
        }
    }

    fun onTitle(value: String) {
        _state.update { it.copy(title = value) }
    }

    fun onSubject(value: String) {
        _state.update { it.copy(subject = value) }
    }

    fun onDescription(value: String) {
        _state.update { it.copy(description = value) }
    }

    fun upload() {
        val current = _state.value
        if (!current.canUpload) return
        _state.update { it.copy(uploading = true, error = null, notice = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                uploadModel.execute(current.file?.uri, current.title, current.subject, current.description)
            }
            result.fold(
                onSuccess = { model ->
                    _state.update {
                        it.copy(
                            uploading = false,
                            file = null,
                            title = "",
                            subject = "",
                            description = "",
                            models = listOf(model) + it.models,
                            notice = "\"${model.title}\" foi enviado. Libere o modelo para uma turma na aba Turmas.",
                        )
                    }
                },
                onFailure = { error -> fail(error) { it.copy(uploading = false) } },
            )
        }
    }

    fun delete(modelId: String) {
        if (_state.value.deletingId != null) return
        _state.update { it.copy(deletingId = modelId, error = null, notice = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { deleteModel.execute(modelId) }
            result.fold(
                onSuccess = {
                    _state.update { state -> state.copy(deletingId = null, models = state.models.filterNot { it.id == modelId }) }
                },
                onFailure = { error -> fail(error) { it.copy(deletingId = null) } },
            )
        }
    }

    fun preview(model: Model3D, onReady: (Model3D, String) -> Unit) {
        if (_state.value.preparingId != null) return
        _state.update { it.copy(preparingId = model.id, preparingProgress = 0f, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                prepareModel.execute(model) { progress -> _state.update { it.copy(preparingProgress = progress) } }
            }
            result.fold(
                onSuccess = { uri ->
                    _state.update { it.copy(preparingId = null) }
                    onReady(model, uri)
                },
                onFailure = { error -> fail(error) { it.copy(preparingId = null) } },
            )
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { listLibrary.execute() }
            result.fold(
                onSuccess = { models -> _state.update { it.copy(loading = false, models = models) } },
                onFailure = { error -> fail(error) { it.copy(loading = false) } },
            )
        }
    }

    private fun fail(error: Throwable, reset: (TeacherLibraryUiState) -> TeacherLibraryUiState) {
        Log.w(TAG, "library request failed", error)
        _state.update { reset(it).copy(error = ErrorMessagePolicy.describe(error)) }
    }

    private companion object {
        const val TAG = "ClaramenteLibrary"
    }
}
