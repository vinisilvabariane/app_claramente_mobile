package com.claramente.feature.classroom.viewmodel

import com.claramente.core.domain.contract.IJoinClassroomUseCase
import com.claramente.core.domain.contract.ILeaveClassroomUseCase
import com.claramente.core.domain.contract.IListClassroomModelsUseCase
import com.claramente.core.domain.contract.IListClassroomsUseCase
import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.domain.policy.ErrorMessagePolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.feature.classroom.state.StudentClassroomUiState
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

class StudentClassroomViewModel(
    private val listClassrooms: IListClassroomsUseCase,
    private val joinClassroom: IJoinClassroomUseCase,
    private val leaveClassroom: ILeaveClassroomUseCase,
    private val listModels: IListClassroomModelsUseCase,
    private val prepareModel: IPrepareModelUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(StudentClassroomUiState())
    val state: StateFlow<StudentClassroomUiState> = _state.asStateFlow()

    fun load() {
        _state.value = StudentClassroomUiState(loading = true)
        refresh(preferred = null)
    }

    fun onCode(value: String) {
        _state.update { it.copy(code = value.take(MAX_CODE_INPUT)) }
    }

    fun join() {
        val current = _state.value
        if (!current.canJoin) return
        _state.update { it.copy(joining = true, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { joinClassroom.execute(current.code) }
            result.fold(
                onSuccess = { joined ->
                    _state.update { it.copy(joining = false, code = "") }
                    refresh(preferred = joined.id)
                },
                onFailure = { error -> fail(error) { it.copy(joining = false) } },
            )
        }
    }

    fun select(classroomId: String) {
        if (_state.value.selectedId == classroomId) return
        _state.update { it.copy(selectedId = classroomId, models = emptyList()) }
        loadModels(classroomId)
    }

    fun leave() {
        val classroomId = _state.value.selectedId ?: return
        if (_state.value.leaving) return
        _state.update { it.copy(leaving = true, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { leaveClassroom.execute(classroomId) }
            result.fold(
                onSuccess = {
                    _state.update { it.copy(leaving = false, selectedId = null, models = emptyList()) }
                    refresh(preferred = null)
                },
                onFailure = { error -> fail(error) { it.copy(leaving = false) } },
            )
        }
    }

    fun openModel(model: Model3D, onReady: (Model3D, String) -> Unit) {
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

    private fun refresh(preferred: String?) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { listClassrooms.execute() }
            result.fold(
                onSuccess = { classrooms ->
                    val previous = _state.value.selectedId
                    val selected = listOfNotNull(preferred, previous).firstOrNull { id -> classrooms.any { it.id == id } }
                        ?: classrooms.firstOrNull()?.id
                    _state.update { it.copy(loading = false, classrooms = classrooms, selectedId = selected) }
                    if (selected != null) loadModels(selected) else _state.update { it.copy(models = emptyList()) }
                },
                onFailure = { error -> fail(error) { it.copy(loading = false) } },
            )
        }
    }

    private fun loadModels(classroomId: String) {
        _state.update { it.copy(modelsLoading = true) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { listModels.execute(classroomId) }
            if (_state.value.selectedId != classroomId) return@launch
            result.fold(
                onSuccess = { models -> _state.update { it.copy(modelsLoading = false, models = models) } },
                onFailure = { error -> fail(error) { it.copy(modelsLoading = false) } },
            )
        }
    }

    private fun fail(error: Throwable, reset: (StudentClassroomUiState) -> StudentClassroomUiState) {
        Log.w(TAG, "classroom request failed", error)
        _state.update { reset(it).copy(error = ErrorMessagePolicy.describe(error)) }
    }

    private companion object {
        const val TAG = "ClaramenteClassroom"
        const val MAX_CODE_INPUT = 12
    }
}
