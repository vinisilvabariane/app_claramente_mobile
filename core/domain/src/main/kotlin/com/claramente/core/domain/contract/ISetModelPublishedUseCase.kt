package com.claramente.core.domain.contract

interface ISetModelPublishedUseCase {
    suspend fun execute(classroomId: String, modelId: String, published: Boolean): Result<Unit>
}
