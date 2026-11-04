package com.claramente.feature.teacher.state

data class TeacherClassroomsActions(
    val onRefresh: () -> Unit,
    val onName: (String) -> Unit,
    val onSubject: (String) -> Unit,
    val onCreate: () -> Unit,
    val onOpen: (String) -> Unit,
    val onBack: () -> Unit,
    val onRegenerateCode: () -> Unit,
    val onRemoveStudent: (String) -> Unit,
    val onTogglePublished: (String, Boolean) -> Unit,
    val onDelete: () -> Unit,
    val onGoToLibrary: () -> Unit,
)
