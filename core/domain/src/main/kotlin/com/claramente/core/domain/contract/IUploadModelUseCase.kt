package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Model3D

interface IUploadModelUseCase {
    suspend fun execute(uri: String?, title: String, subject: String?, description: String?): Result<Model3D>
}
