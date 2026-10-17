package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.domain.contract.IListLibraryUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IModelClient

class ListLibraryUseCase(
    private val modelClient: IModelClient,
    private val sessions: SessionController,
) : IListLibraryUseCase {
    override suspend fun execute(): Result<List<Model3D>> = runCatching {
        modelClient.library(OnlineSessionPolicy.require(sessions))
    }
}
