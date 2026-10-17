package com.claramente.core.network.http

import com.claramente.core.network.mapper.AuthJsonMapper
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ClaramenteHttp(
    private val baseUrl: String,
    private val onRefreshed: (AuthSession) -> Unit = {},
    private val onRejected: () -> Unit = {},
) {
    private val apiUrl = baseUrl.trimEnd('/') + "/api"

    private val publicClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .callTimeout(15, TimeUnit.SECONDS)
        .build()

    private val client = publicClient.newBuilder()
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .callTimeout(90, TimeUnit.SECONDS)
        .build()

    private val transferClient = publicClient.newBuilder()
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.MINUTES)
        .build()

    internal fun post(path: String, body: JSONObject, bearer: String?): JSONObject {
        val builder = Request.Builder()
            .url(apiUrl + path)
            .post(body.toString().toRequestBody(JSON))
        if (bearer != null) builder.header("Authorization", "Bearer $bearer")
        val chosen = if (bearer != null) client else publicClient
        return text(builder.build(), chosen).let { if (it.isBlank()) JSONObject() else JSONObject(it) }
    }

    internal fun send(method: String, path: String, body: JSONObject?, bearer: String): String {
        val payload: RequestBody? = body?.toString()?.toRequestBody(JSON)
            ?: if (method == "POST" || method == "PUT") ByteArray(0).toRequestBody(null) else null
        val request = Request.Builder()
            .url(apiUrl + path)
            .method(method, payload)
            .header("Authorization", "Bearer $bearer")
            .build()
        return text(request, client)
    }

    internal fun download(path: String, bearer: String, onProgress: (Long, Long) -> Unit): ByteArray {
        val request = Request.Builder()
            .url(apiUrl + path)
            .get()
            .header("Authorization", "Bearer $bearer")
            .build()
        transferClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw failure(response.code, response.body.bytes())
            val total = response.body.contentLength()
            val output = ByteArrayOutputStream(if (total in 1..Int.MAX_VALUE) total.toInt() else DOWNLOAD_BUFFER)
            response.body.byteStream().use { input ->
                val buffer = ByteArray(DOWNLOAD_BUFFER)
                var received = 0L
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    output.write(buffer, 0, count)
                    received += count
                    onProgress(received, total)
                }
            }
            return output.toByteArray()
        }
    }

    internal fun upload(path: String, fields: Map<String, String?>, fileName: String, content: ByteArray, bearer: String): String {
        val form = MultipartBody.Builder().setType(MultipartBody.FORM)
        fields.forEach { (name, value) -> if (value != null) form.addFormDataPart(name, value) }
        form.addFormDataPart("file", fileName, content.toRequestBody(GLB))
        val request = Request.Builder()
            .url(apiUrl + path)
            .post(form.build())
            .header("Authorization", "Bearer $bearer")
            .build()
        return text(request, transferClient)
    }

    internal fun <T> authenticated(session: AuthSession, call: (String) -> T): T {
        val used = session.token
        if (isExpired(used)) {
            renewOrReject(session, used)
            return call(session.token)
        }
        return try {
            call(used)
        } catch (expired: ApiException) {
            if (expired.status != 401) throw expired
            renewOrReject(session, used)
            call(session.token)
        }
    }

    private fun refresh(session: AuthSession): Boolean {
        val json = try {
            post("/auth/refresh", JSONObject().put("refreshToken", session.refreshToken), bearer = null)
        } catch (rejected: ApiException) {
            if (rejected.status == 401 || rejected.status == 403) return false
            throw rejected
        }
        val renewed = AuthJsonMapper.session(json)
        session.token = renewed.token
        session.refreshToken = renewed.refreshToken
        session.refreshExpires = renewed.refreshExpires
        onRefreshed(session)
        return true
    }

    private fun renewOrReject(session: AuthSession, staleToken: String) {
        if (refreshOnce(session, staleToken)) return
        onRejected()
        throw ApiException(401, SESSION_EXPIRED)
    }

    private fun refreshOnce(session: AuthSession, staleToken: String): Boolean = synchronized(session) {
        if (session.token != staleToken) return true
        refresh(session)
    }

    private fun isExpired(token: String): Boolean = TokenExpiryReader.isExpired(token, System.currentTimeMillis())

    private fun text(request: Request, chosen: OkHttpClient): String = String(bytes(request, chosen), Charsets.UTF_8)

    private fun bytes(request: Request, chosen: OkHttpClient): ByteArray =
        chosen.newCall(request).execute().use { response ->
            val content = response.body.bytes()
            if (!response.isSuccessful) throw failure(response.code, content)
            content
        }

    private fun failure(code: Int, content: ByteArray): ApiException {
        val envelope = runCatching { JSONObject(String(content, Charsets.UTF_8)) }.getOrNull()
        val message = envelope?.optString("error")?.takeIf { it.isNotEmpty() } ?: "HTTP $code"
        return ApiException(code, message)
    }

    private companion object {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val GLB = "model/gltf-binary".toMediaType()
        const val SESSION_EXPIRED = "Sessão expirada. Entre de novo."
        const val DOWNLOAD_BUFFER = 64 * 1024
    }
}
