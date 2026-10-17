package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.DocumentInfo

interface IInspectModelFileUseCase {
    suspend fun execute(uri: String): Result<DocumentInfo>
}
