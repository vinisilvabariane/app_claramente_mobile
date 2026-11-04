package com.claramente.core.domain.contract

interface IDeleteModelUseCase {
    suspend fun execute(modelId: String): Result<Unit>
}
