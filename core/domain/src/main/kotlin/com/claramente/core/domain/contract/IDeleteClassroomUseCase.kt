package com.claramente.core.domain.contract

interface IDeleteClassroomUseCase {
    suspend fun execute(classroomId: String): Result<Unit>
}
