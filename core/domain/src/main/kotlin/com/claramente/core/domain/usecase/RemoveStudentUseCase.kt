package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IRemoveStudentUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.network.contract.IClassroomClient

class RemoveStudentUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IRemoveStudentUseCase {
    override suspend fun execute(classroomId: String, studentId: String): Result<Unit> = runCatching {
        classroomClient.removeStudent(OnlineSessionPolicy.require(sessions), classroomId, studentId)
    }
}
