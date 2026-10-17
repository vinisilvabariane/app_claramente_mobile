package com.claramente.feature.teacher.viewmodel

import com.claramente.core.domain.contract.ICreateClassroomUseCase
import com.claramente.core.domain.contract.IDeleteClassroomUseCase
import com.claramente.core.domain.contract.IListClassroomsUseCase
import com.claramente.core.domain.contract.IListLibraryUseCase
import com.claramente.core.domain.contract.IListStudentsUseCase
import com.claramente.core.domain.contract.IRegenerateJoinCodeUseCase
import com.claramente.core.domain.contract.IRemoveStudentUseCase
import com.claramente.core.domain.contract.ISetModelPublishedUseCase
import com.claramente.core.domain.policy.ErrorMessagePolicy
import com.claramente.feature.teacher.state.TeacherClassroomsUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherClassroomsViewModel(
    private val listClassrooms: IListClassroomsUseCase,
    private val createClassroom: ICreateClassroomUseCase,
    private val deleteClassroom: IDeleteClassroomUseCase,
    private val regenerateJoinCode: IRegenerateJoinCodeUseCase,
    private val listStudents: IListStudentsUseCase,
    private val removeStudent: IRemoveStudentUseCase,
    private val listLibrary: IListLibraryUseCase,
    private val setModelPublished: ISetModelPublishedUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TeacherClassroomsUiState())
    val state: StateFlow<TeacherClassroomsUiState> = _state.asStateFlow()

    fun load() {
        val selected = _state.value.selectedId
        _state.value = TeacherClassroomsUiState(loading = true, selectedId = selected)
        refreshList()
        selected?.let(::loadDetail)
    }

    fun clear() {
        _state.value = TeacherClassroomsUiState()
    }

    fun onName(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun onSubject(value: String) {
        _state.update { it.copy(subject = value) }
    }

    fun create() {
        val current = _state.value
        if (!current.canCreate) return
        _state.update { it.copy(creating = true, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { createClassroom.execute(current.name, current.subject) }
            result.fold(
                onSuccess = { created ->
                    _state.update {
                        it.copy(creating = false, name = "", subject = "", classrooms = (it.classrooms + created).sortedBy { c -> c.name })
                    }
                    open(created.id)
                },
                onFailure = { error -> fail(error) { it.copy(creating = false) } },
            )
        }
    }

    fun open(classroomId: String) {
        _state.update { it.copy(selectedId = classroomId, students = emptyList(), error = null) }
        loadDetail(classroomId)
    }

    fun back() {
        _state.update { it.copy(selectedId = null, students = emptyList(), error = null) }
        refreshList()
    }

    fun regenerateCode() {
        val classroomId = _state.value.selectedId ?: return
        _state.update { it.copy(regenerating = true, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { regenerateJoinCode.execute(classroomId) }
            result.fold(
                onSuccess = { updated ->
                    _state.update { state ->
                        state.copy(regenerating = false, classrooms = state.classrooms.map { if (it.id == updated.id) updated else it })
                    }
                },
                onFailure = { error -> fail(error) { it.copy(regenerating = false) } },
            )
        }
    }

    fun removeStudent(studentId: String) {
        val classroomId = _state.value.selectedId ?: return
        _state.update { it.copy(removingStudentId = studentId, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { removeStudent.execute(classroomId, studentId) }
            result.fold(
                onSuccess = {
                    _state.update { state ->
                        state.copy(removingStudentId = null, students = state.students.filterNot { it.id == studentId })
                    }
                    refreshList()
                },
                onFailure = { error -> fail(error) { it.copy(removingStudentId = null) } },
            )
        }
    }

    fun togglePublished(modelId: String, published: Boolean) {
        val classroomId = _state.value.selectedId ?: return
        if (modelId in _state.value.busyModelIds) return
        _state.update { it.copy(busyModelIds = it.busyModelIds + modelId, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { setModelPublished.execute(classroomId, modelId, published) }
            result.fold(
                onSuccess = {
                    _state.update { state ->
                        state.copy(
                            busyModelIds = state.busyModelIds - modelId,
                            library = state.library.map { model ->
                                if (model.id != modelId) {
                                    model
                                } else {
                                    val others = model.classroomIds - classroomId
                                    model.copy(classroomIds = if (published) others + classroomId else others)
                                }
                            },
                        )
                    }
                    refreshList()
                },
                onFailure = { error -> fail(error) { it.copy(busyModelIds = it.busyModelIds - modelId) } },
            )
        }
    }

    fun delete() {
        val classroomId = _state.value.selectedId ?: return
        _state.update { it.copy(deleting = true, error = null) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { deleteClassroom.execute(classroomId) }
            result.fold(
                onSuccess = {
                    _state.update { state ->
                        state.copy(deleting = false, selectedId = null, classrooms = state.classrooms.filterNot { it.id == classroomId })
                    }
                },
                onFailure = { error -> fail(error) { it.copy(deleting = false) } },
            )
        }
    }

    private fun refreshList() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { listClassrooms.execute() }
            result.fold(
                onSuccess = { classrooms -> _state.update { it.copy(loading = false, classrooms = classrooms) } },
                onFailure = { error -> fail(error) { it.copy(loading = false) } },
            )
        }
    }

    private fun loadDetail(classroomId: String) {
        _state.update { it.copy(detailLoading = true) }
        viewModelScope.launch {
            val students = async(Dispatchers.IO) { listStudents.execute(classroomId) }
            val library = async(Dispatchers.IO) { listLibrary.execute() }
            val studentsResult = students.await()
            val libraryResult = library.await()
            if (_state.value.selectedId != classroomId) return@launch
            _state.update {
                it.copy(
                    detailLoading = false,
                    students = studentsResult.getOrDefault(it.students),
                    library = libraryResult.getOrDefault(it.library),
                )
            }
            (studentsResult.exceptionOrNull() ?: libraryResult.exceptionOrNull())?.let { error -> fail(error) { it } }
        }
    }

    private fun fail(error: Throwable, reset: (TeacherClassroomsUiState) -> TeacherClassroomsUiState) {
        Log.w(TAG, "teacher classroom request failed", error)
        _state.update { reset(it).copy(error = ErrorMessagePolicy.describe(error)) }
    }

    private companion object {
        const val TAG = "ClaramenteTeacher"
    }
}
