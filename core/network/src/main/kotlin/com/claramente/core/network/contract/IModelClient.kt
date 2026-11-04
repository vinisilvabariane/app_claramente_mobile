package com.claramente.core.network.contract

import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.http.AuthSession

interface IModelClient {
    fun library(session: AuthSession): List<Model3D>

    fun upload(session: AuthSession, title: String, subject: String?, description: String?, fileName: String, content: ByteArray): Model3D

    fun delete(session: AuthSession, modelId: String)

    fun download(session: AuthSession, modelId: String, onProgress: (received: Long, total: Long) -> Unit): ByteArray
}
