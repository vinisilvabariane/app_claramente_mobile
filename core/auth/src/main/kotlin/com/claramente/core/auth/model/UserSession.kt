package com.claramente.core.auth.model

import com.claramente.core.model.auth.UserRole
import com.claramente.core.network.http.AuthSession

data class UserSession(
    val auth: AuthSession,
    val id: String,
    val email: String,
    val name: String,
    val roles: List<String>,
    val role: UserRole,
    val department: String?,
    val registration: String?,
    val expiresAt: Long,
) {
    val firstName: String
        get() = name.trim().split(Regex("\\s+")).firstOrNull()?.takeIf { it.isNotBlank() } ?: "Você"

    val initials: String
        get() {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
            val first = parts.firstOrNull()?.first()?.toString() ?: ""
            val last = if (parts.size > 1) parts.last().first().toString() else ""
            return (first + last).uppercase().ifEmpty { "?" }
        }

    val isTeacher: Boolean
        get() = role == UserRole.TEACHER
}
