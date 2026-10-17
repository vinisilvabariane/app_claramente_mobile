package com.claramente.feature.teacher.state

import com.claramente.core.model.classroom.Model3D

data class TeacherLibraryActions(
    val onRefresh: () -> Unit,
    val onPickFile: (String) -> Unit,
    val onTitle: (String) -> Unit,
    val onSubject: (String) -> Unit,
    val onDescription: (String) -> Unit,
    val onUpload: () -> Unit,
    val onDelete: (String) -> Unit,
    val onPreview: (Model3D) -> Unit,
)
