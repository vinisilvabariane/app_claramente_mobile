package com.claramente.feature.teacher.state

import com.claramente.core.model.classroom.DocumentInfo
import com.claramente.core.model.classroom.Model3D

data class TeacherLibraryUiState(
    val loading: Boolean = true,
    val models: List<Model3D> = emptyList(),
    val file: DocumentInfo? = null,
    val inspecting: Boolean = false,
    val title: String = "",
    val subject: String = "",
    val description: String = "",
    val uploading: Boolean = false,
    val deletingId: String? = null,
    val preparingId: String? = null,
    val preparingProgress: Float = 0f,
    val error: String? = null,
    val notice: String? = null,
) {
    val canUpload: Boolean
        get() = file != null && title.isNotBlank() && !uploading && !inspecting
}
