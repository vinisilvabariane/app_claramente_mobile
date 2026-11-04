package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IJoinClassroomUseCase
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.domain.policy.CodePolicy
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Classroom
import com.claramente.core.network.contract.IClassroomClient

class JoinClassroomUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IJoinClassroomUseCase {
    override suspend fun execute(code: String): Result<Classroom> = runCatching {
        require(CodePolicy.isValid(code)) { ClassroomErrors.INVALID_CODE }
        classroomClient.join(OnlineSessionPolicy.require(sessions), CodePolicy.normalize(code))
    }
}
