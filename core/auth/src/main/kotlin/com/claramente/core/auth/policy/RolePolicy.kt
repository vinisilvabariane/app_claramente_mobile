package com.claramente.core.auth.policy

import com.claramente.core.model.auth.UserRole

object RolePolicy {
    const val STUDENT_ROLE = "student"
    const val TEACHER_ROLE = "admin"

    fun resolve(roles: List<String>): UserRole =
        if (roles.any { it.trim().equals(TEACHER_ROLE, ignoreCase = true) }) UserRole.TEACHER else UserRole.STUDENT
}
