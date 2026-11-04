package com.claramente.feature.teacher.state

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D

data class TeacherClassroomsUiState(
    val loading: Boolean = true,
    val classrooms: List<Classroom> = emptyList(),
    val name: String = "",
    val subject: String = "",
    val creating: Boolean = false,
    val selectedId: String? = null,
    val students: List<ClassroomStudent> = emptyList(),
    val library: List<Model3D> = emptyList(),
    val detailLoading: Boolean = false,
    val busyModelIds: Set<String> = emptySet(),
    val removingStudentId: String? = null,
    val regenerating: Boolean = false,
    val deleting: Boolean = false,
    val error: String? = null,
) {
    val selected: Classroom?
        get() = classrooms.firstOrNull { it.id == selectedId }

    val canCreate: Boolean
        get() = name.isNotBlank() && !creating

    fun isPublished(model: Model3D): Boolean = selectedId != null && selectedId in model.classroomIds
}
