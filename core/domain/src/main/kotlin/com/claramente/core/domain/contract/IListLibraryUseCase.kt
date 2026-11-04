package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Model3D

interface IListLibraryUseCase {
    suspend fun execute(): Result<List<Model3D>>
}
