package com.claramente.core.domain.usecase

import com.claramente.core.domain.contract.IPrepareModelUseCase
import com.claramente.core.model.classroom.Model3D
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrepareModelsUseCaseTest {
    private fun model(id: String) = Model3D(id, "Modelo $id", null, null, 20, "abc", "Carla", emptyList())

    private val prepareOne = object : IPrepareModelUseCase {
        override suspend fun execute(model: Model3D, onProgress: (Float) -> Unit): Result<String> =
            if (model.id == "quebrado") Result.failure(IllegalStateException("falhou")) else Result.success("file://${model.id}")
    }

    @Test
    fun `prepara todos e informa o progresso`() = runBlocking {
        val progress = mutableListOf<Pair<Int, Int>>()
        val result = PrepareModelsUseCase(prepareOne).execute(listOf(model("a"), model("b"))) { done, total -> progress += done to total }
        assertEquals(listOf("file://a", "file://b"), result.getOrThrow().map { it.fileUri })
        assertEquals(listOf(0 to 2, 1 to 2, 2 to 2), progress)
    }

    @Test
    fun `um modelo com falha nao impede os outros`() = runBlocking {
        val result = PrepareModelsUseCase(prepareOne).execute(listOf(model("a"), model("quebrado"), model("c")))
        assertEquals(listOf("a", "c"), result.getOrThrow().map { it.model.id })
    }

    @Test
    fun `falha quando nenhum modelo fica pronto`() = runBlocking {
        val result = PrepareModelsUseCase(prepareOne).execute(listOf(model("quebrado")))
        assertTrue(result.isFailure)
    }

    @Test
    fun `lista vazia termina sem erro`() = runBlocking {
        assertEquals(emptyList<Any>(), PrepareModelsUseCase(prepareOne).execute(emptyList()).getOrThrow())
    }
}
