package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.data.contract.IDocumentReader
import com.claramente.core.domain.contract.IUploadModelUseCase
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.domain.policy.ModelFilePolicy
import com.claramente.core.domain.policy.OnlineSessionPolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IModelClient

class UploadModelUseCase(
    private val modelClient: IModelClient,
    private val documentReader: IDocumentReader,
    private val sessions: SessionController,
) : IUploadModelUseCase {
    override suspend fun execute(uri: String?, title: String, subject: String?, description: String?): Result<Model3D> = runCatching {
        require(title.isNotBlank()) { ClassroomErrors.EMPTY_TITLE }
        requireNotNull(uri) { ClassroomErrors.MISSING_FILE }
        val info = documentReader.inspect(uri)
        val content = runCatching { documentReader.readAll(uri) }
            .getOrElse { throw IllegalStateException(ClassroomErrors.FILE_UNREADABLE) }
        require(ModelFilePolicy.fits(content.size.toLong())) { ClassroomErrors.FILE_TOO_LARGE }
        require(ModelFilePolicy.isGlb(content.copyOf(ModelFilePolicy.HEADER_BYTES), content.size.toLong())) { ClassroomErrors.NOT_GLB }
        modelClient.upload(
            OnlineSessionPolicy.require(sessions),
            title.trim(),
            subject?.trim()?.takeIf { it.isNotEmpty() },
            description?.trim()?.takeIf { it.isNotEmpty() },
            info.name,
            content,
        )
    }
}
