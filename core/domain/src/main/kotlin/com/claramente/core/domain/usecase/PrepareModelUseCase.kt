package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.data.contract.IModelFileStore
import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IModelClient

class PrepareModelUseCase(
    private val modelClient: IModelClient,
    private val fileStore: IModelFileStore,
    private val sessions: SessionController,
) : IPrepareModelUseCase {
    override suspend fun execute(model: Model3D, onProgress: (Float) -> Unit): Result<String> = runCatching {
        fileStore.cached(model.id, model.sha256)?.let { return@runCatching it }
        var reported = -1
        val content = modelClient.download(OnlineSessionPolicy.require(sessions), model.id) { received, total ->
            val expected = if (total > 0) total else model.sizeBytes
            val percent = if (expected > 0) (received * 100 / expected).toInt().coerceIn(0, 100) else 0
            if (percent != reported) {
                reported = percent
                onProgress(percent / 100f)
            }
        }
        fileStore.save(model.id, model.sha256, content)
    }
}
