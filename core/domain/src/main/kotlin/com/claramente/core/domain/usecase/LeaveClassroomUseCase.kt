package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.ILeaveClassroomUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.network.contract.IClassroomClient

class LeaveClassroomUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : ILeaveClassroomUseCase {
    override suspend fun execute(classroomId: String): Result<Unit> = runCatching {
        classroomClient.leave(OnlineSessionPolicy.require(sessions), classroomId)
    }
}
