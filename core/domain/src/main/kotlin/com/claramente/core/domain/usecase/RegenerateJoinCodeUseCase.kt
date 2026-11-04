package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IRegenerateJoinCodeUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Classroom
import com.claramente.core.network.contract.IClassroomClient

class RegenerateJoinCodeUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IRegenerateJoinCodeUseCase {
    override suspend fun execute(classroomId: String): Result<Classroom> = runCatching {
        classroomClient.regenerateCode(OnlineSessionPolicy.require(sessions), classroomId)
    }
}
