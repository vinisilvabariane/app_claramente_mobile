package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Model3D

interface IPrepareModelUseCase {
    suspend fun execute(model: Model3D, onProgress: (Float) -> Unit = {}): Result<String>
}
