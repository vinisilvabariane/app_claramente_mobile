package com.claramente.core.model.classroom

data class Classroom(
    val id: String,
    val name: String,
    val subject: String?,
    val joinCode: String?,
    val teacherName: String,
    val studentCount: Int,
    val modelCount: Int,
)
