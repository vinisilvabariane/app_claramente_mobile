package com.claramente.core.domain.contract

interface ILeaveClassroomUseCase {
    suspend fun execute(classroomId: String): Result<Unit>
}
