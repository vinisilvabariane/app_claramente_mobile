package com.claramente.core.domain.usecase

import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.domain.contract.IPrepareModelsUseCase
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.model.classroom.PreparedModel

class PrepareModelsUseCase(private val prepareModel: IPrepareModelUseCase) : IPrepareModelsUseCase {
    override suspend fun execute(models: List<Model3D>, onProgress: (done: Int, total: Int) -> Unit): Result<List<PreparedModel>> {
        val prepared = mutableListOf<PreparedModel>()
        var firstError: Throwable? = null
        onProgress(0, models.size)
        models.forEachIndexed { index, model ->
            prepareModel.execute(model).fold(
                onSuccess = { uri -> prepared += PreparedModel(model, uri) },
                onFailure = { error -> if (firstError == null) firstError = error },
            )
            onProgress(index + 1, models.size)
        }
        val error = firstError
        return if (prepared.isEmpty() && error != null) Result.failure(error) else Result.success(prepared)
    }
}
