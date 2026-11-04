package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.ICreateClassroomUseCase
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Classroom
import com.claramente.core.network.contract.IClassroomClient

class CreateClassroomUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : ICreateClassroomUseCase {
    override suspend fun execute(name: String, subject: String?): Result<Classroom> = runCatching {
        require(name.isNotBlank()) { ClassroomErrors.EMPTY_NAME }
        classroomClient.create(OnlineSessionPolicy.require(sessions), name.trim(), subject?.trim()?.takeIf { it.isNotEmpty() })
    }
}
