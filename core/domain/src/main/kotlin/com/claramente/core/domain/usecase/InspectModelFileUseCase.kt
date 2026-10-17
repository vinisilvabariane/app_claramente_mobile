package com.claramente.core.domain.usecase

import com.claramente.core.data.contract.IDocumentReader
import com.claramente.core.domain.contract.IInspectModelFileUseCase
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.domain.policy.ModelFilePolicy
import com.claramente.core.model.classroom.DocumentInfo

class InspectModelFileUseCase(
    private val documentReader: IDocumentReader,
) : IInspectModelFileUseCase {
    override suspend fun execute(uri: String): Result<DocumentInfo> = runCatching {
        val info = documentReader.inspect(uri)
        require(info.sizeBytes < 0 || ModelFilePolicy.fits(info.sizeBytes)) { ClassroomErrors.FILE_TOO_LARGE }
        val header = runCatching { documentReader.readHeader(uri, ModelFilePolicy.HEADER_BYTES) }
            .getOrElse { throw IllegalStateException(ClassroomErrors.FILE_UNREADABLE) }
        require(ModelFilePolicy.isGlb(header, info.sizeBytes)) { ClassroomErrors.NOT_GLB }
        info
    }
}
