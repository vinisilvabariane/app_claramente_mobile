package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IListClassroomsUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Classroom
import com.claramente.core.network.contract.IClassroomClient

class ListClassroomsUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IListClassroomsUseCase {
    override suspend fun execute(): Result<List<Classroom>> = runCatching {
        classroomClient.list(OnlineSessionPolicy.require(sessions))
    }
}
