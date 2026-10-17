package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IListStudentsUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.network.contract.IClassroomClient

class ListStudentsUseCase(
    private val classroomClient: IClassroomClient,
    private val sessions: SessionController,
) : IListStudentsUseCase {
    override suspend fun execute(classroomId: String): Result<List<ClassroomStudent>> = runCatching {
        classroomClient.students(OnlineSessionPolicy.require(sessions), classroomId)
    }
}
