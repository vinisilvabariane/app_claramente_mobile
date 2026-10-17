package com.claramente.core.domain.policy

object CodePolicy {
    const val LENGTH = 6

    fun normalize(code: String): String = code.filter { it.isLetterOrDigit() }.uppercase()

    fun isValid(code: String): Boolean = normalize(code).length == LENGTH
}
