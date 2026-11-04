package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IListClassroomModelsUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IClassroomClient

class ListClassroomModelsUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IListClassroomModelsUseCase {
    override suspend fun execute(classroomId: String): Result<List<Model3D>> = runCatching {
        classroomClient.models(OnlineSessionPolicy.require(sessions), classroomId)
    }
}
