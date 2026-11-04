package com.claramente.feature.classroom.state

import com.claramente.core.model.classroom.Model3D

data class StudentClassroomActions(
    val onRefresh: () -> Unit,
    val onCode: (String) -> Unit,
    val onJoin: () -> Unit,
    val onSelect: (String) -> Unit,
    val onOpenModel: (Model3D) -> Unit,
    val onLeave: () -> Unit,
)
