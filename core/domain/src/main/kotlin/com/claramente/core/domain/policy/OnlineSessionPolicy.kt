package com.claramente.core.domain.policy

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.auth.policy.MockTokenPolicy
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.network.http.AuthSession

object OnlineSessionPolicy {
    fun require(sessions: SessionController): AuthSession {
        val session = sessions.requireAuth()
        check(!MockTokenPolicy.isMock(session.token)) { ClassroomErrors.MOCK_MODE }
        return session
    }
}
