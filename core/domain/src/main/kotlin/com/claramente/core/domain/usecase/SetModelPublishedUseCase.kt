package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.ISetModelPublishedUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.network.contract.IClassroomClient

class SetModelPublishedUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : ISetModelPublishedUseCase {
    override suspend fun execute(classroomId: String, modelId: String, published: Boolean): Result<Unit> = runCatching {
        val session = OnlineSessionPolicy.require(sessions)
        if (published) {
            classroomClient.publish(session, classroomId, modelId)
        } else {
            classroomClient.unpublish(session, classroomId, modelId)
        }
    }
}
