package com.claramente.core.auth.policy

import com.claramente.core.auth.mapper.Base64UrlMapper
import com.claramente.core.model.auth.AuthTokens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import org.json.JSONArray
import org.json.JSONObject

object MockTokenPolicy {
    private const val SEVEN_DAYS_SECONDS = 7L * 24 * 60 * 60
    private const val SIGNATURE = "mock-signature"

    fun tokens(email: String?, nowMs: Long): AuthTokens {
        val issuedAt = nowMs / 1000
        val header = JSONObject()
            .put("alg", "none")
            .put("typ", "JWT")
        val payload = JSONObject()
            .put("sub", "mock-user-0001")
            .put("email", email ?: "teste@claramente.local")
            .put("name", "Teste Local")
            .put("roles", JSONArray().put(RolePolicy.STUDENT_ROLE))
            .put("department", "Desenvolvimento")
            .put("registration", "000000")
            .put("iat", issuedAt)
            .put("nbf", issuedAt)
            .put("exp", issuedAt + SEVEN_DAYS_SECONDS)
            .put("jti", "mock-$issuedAt")
        val token = "${encode(header)}.${encode(payload)}.$SIGNATURE"
        return AuthTokens(token, "mock-refresh-token", isoUtc(nowMs + SEVEN_DAYS_SECONDS * 1000))
    }

    fun isMock(token: String?): Boolean = token?.endsWith(".$SIGNATURE") == true

    private fun encode(json: JSONObject): String = Base64UrlMapper.encode(json.toString().toByteArray(Charsets.UTF_8))

    private fun isoUtc(epochMs: Long): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date(epochMs))
}
