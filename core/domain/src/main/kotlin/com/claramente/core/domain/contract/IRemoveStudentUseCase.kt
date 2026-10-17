package com.claramente.core.domain.contract

interface IRemoveStudentUseCase {
    suspend fun execute(classroomId: String, studentId: String): Result<Unit>
}
