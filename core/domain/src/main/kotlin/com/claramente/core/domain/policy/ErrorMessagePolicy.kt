package com.claramente.core.domain.policy

import com.claramente.core.domain.error.AuthErrors
import com.claramente.core.network.http.ApiException
import java.io.IOException

object ErrorMessagePolicy {
    fun describe(error: Throwable): String = when (error) {
        is ApiException -> error.message ?: AuthErrors.REQUEST_FAILED
        is IOException -> AuthErrors.REQUEST_FAILED
        is IllegalArgumentException, is IllegalStateException -> error.message ?: AuthErrors.UNEXPECTED
        else -> AuthErrors.UNEXPECTED
    }
}
