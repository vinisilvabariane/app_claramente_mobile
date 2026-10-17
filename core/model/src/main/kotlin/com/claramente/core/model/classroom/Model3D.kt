package com.claramente.core.model.classroom

data class Model3D(
    val id: String,
    val title: String,
    val subject: String?,
    val description: String?,
    val sizeBytes: Long,
    val sha256: String,
    val ownerName: String,
    val classroomIds: List<String>,
)
