package com.claramente.core.network.client

import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IModelClient
import com.claramente.core.network.http.AuthSession
import com.claramente.core.network.http.ClaramenteHttp
import com.claramente.core.network.mapper.ClassroomJsonMapper
import org.json.JSONObject

class HttpModelClient(private val http: ClaramenteHttp) : IModelClient {
    override fun library(session: AuthSession): List<Model3D> =
        ClassroomJsonMapper.models(http.authenticated(session) { token -> http.send("GET", ROOT, null, token) })

    override fun upload(
        session: AuthSession,
        title: String,
        subject: String?,
        description: String?,
        fileName: String,
        content: ByteArray,
    ): Model3D {
        val fields = mapOf("title" to title, "subject" to subject, "description" to description)
        val raw = http.authenticated(session) { token -> http.upload(ROOT, fields, fileName, content, token) }
        return ClassroomJsonMapper.model(JSONObject(raw))
    }

    override fun delete(session: AuthSession, modelId: String) {
        http.authenticated(session) { token -> http.send("DELETE", "$ROOT/$modelId", null, token) }
    }

    override fun download(session: AuthSession, modelId: String, onProgress: (received: Long, total: Long) -> Unit): ByteArray =
        http.authenticated(session) { token -> http.download("$ROOT/$modelId/file", token, onProgress) }

    private companion object {
        const val ROOT = "/models"
    }
}
