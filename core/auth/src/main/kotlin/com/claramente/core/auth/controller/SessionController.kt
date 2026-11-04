package com.claramente.core.auth.controller

import com.claramente.core.auth.mapper.JwtClaimsMapper
import com.claramente.core.auth.model.UserSession
import com.claramente.core.auth.policy.RolePolicy
import com.claramente.core.network.http.ApiException
import com.claramente.core.network.http.AuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionController {
    private val _current = MutableStateFlow<UserSession?>(null)
    val current: StateFlow<UserSession?> = _current.asStateFlow()

    private val _checked = MutableStateFlow(false)
    val checked: StateFlow<Boolean> = _checked.asStateFlow()

    val auth: AuthSession?
        get() = _current.value?.auth

    fun requireAuth(): AuthSession = auth ?: throw ApiException(401, SESSION_EXPIRED)

    fun open(auth: AuthSession): UserSession {
        val claims = JwtClaimsMapper.payload(auth.token) ?: throw ApiException(401, "Token inválido.")
        val email = JwtClaimsMapper.text(claims, "email") ?: ""
        val roles = JwtClaimsMapper.list(claims, "roles")
        return UserSession(
            auth = auth,
            id = JwtClaimsMapper.text(claims, "sub") ?: "",
            email = email,
            name = JwtClaimsMapper.text(claims, "name") ?: "",
            roles = roles,
            role = RolePolicy.resolve(roles),
            department = JwtClaimsMapper.text(claims, "department"),
            registration = JwtClaimsMapper.text(claims, "registration"),
            expiresAt = JwtClaimsMapper.expiresAtMs(claims) ?: 0L,
        ).also { _current.value = it }
    }

    fun markChecked() {
        _checked.value = true
    }

    fun clear() {
        _current.value = null
    }

    private companion object {
        const val SESSION_EXPIRED = "Sessão expirada. Entre de novo."
    }
}
