package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Model3D
import com.claramente.core.model.classroom.PreparedModel

interface IPrepareModelsUseCase {
    suspend fun execute(models: List<Model3D>, onProgress: (done: Int, total: Int) -> Unit = { _, _ -> }): Result<List<PreparedModel>>
}
