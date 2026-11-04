package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.data.contract.IModelFileStore
import com.claramente.core.domain.contract.IDeleteModelUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.network.contract.IModelClient

class DeleteModelUseCase(
    private val modelClient: IModelClient,
    private val fileStore: IModelFileStore,
    private val sessions: SessionController,
) : IDeleteModelUseCase {
    override suspend fun execute(modelId: String): Result<Unit> = runCatching {
        modelClient.delete(OnlineSessionPolicy.require(sessions), modelId)
        fileStore.evict(modelId)
    }
}
