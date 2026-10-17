package com.claramente.feature.classroom.state

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.Model3D

data class StudentClassroomUiState(
    val loading: Boolean = true,
    val classrooms: List<Classroom> = emptyList(),
    val selectedId: String? = null,
    val models: List<Model3D> = emptyList(),
    val modelsLoading: Boolean = false,
    val code: String = "",
    val joining: Boolean = false,
    val leaving: Boolean = false,
    val preparingId: String? = null,
    val preparingProgress: Float = 0f,
    val error: String? = null,
) {
    val selected: Classroom?
        get() = classrooms.firstOrNull { it.id == selectedId }

    val canJoin: Boolean
        get() = code.isNotBlank() && !joining
}
