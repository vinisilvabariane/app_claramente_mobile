package com.claramente.core.domain.usecase

import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IModelClient
import com.claramente.core.network.http.AuthSession

internal class FakeModelClient(private val file: ByteArray = ByteArray(0)) : IModelClient {
    val calls = mutableListOf<String>()
    var uploaded: ByteArray? = null

    override fun library(session: AuthSession): List<Model3D> = emptyList()

    override fun upload(
        session: AuthSession,
        title: String,
        subject: String?,
        description: String?,
        fileName: String,
        content: ByteArray,
    ): Model3D {
        calls += "upload:$title:$subject:$fileName"
        uploaded = content
        return Model3D("m1", title, subject, description, content.size.toLong(), "sha", "Carla", emptyList())
    }

    override fun delete(session: AuthSession, modelId: String) {
        calls += "delete:$modelId"
    }

    override fun download(session: AuthSession, modelId: String, onProgress: (received: Long, total: Long) -> Unit): ByteArray {
        calls += "download:$modelId"
        val half = (file.size / 2).toLong()
        onProgress(half, file.size.toLong())
        onProgress(file.size.toLong(), file.size.toLong())
        return file
    }
}
