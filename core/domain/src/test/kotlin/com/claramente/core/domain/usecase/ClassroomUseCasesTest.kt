package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.auth.policy.MockTokenPolicy
import com.claramente.core.auth.policy.RolePolicy
import com.claramente.core.domain.error.ClassroomErrors
import com.claramente.core.domain.policy.ModelFilePolicy
import com.claramente.core.domain.policy.ModelQrPolicy
import com.claramente.core.model.auth.UserRole
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.http.ApiException
import com.claramente.core.network.http.AuthSession
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassroomUseCasesTest {
    private fun signedIn(): SessionController = SessionController().apply {
        val mock = MockTokenPolicy.tokens("ana@claramente.local", System.currentTimeMillis())
        val real = mock.token.substringBeforeLast('.') + ".assinatura-real"
        open(AuthSession(real, "refresh", mock.refreshExpires))
    }

    private fun mockSession(): SessionController = SessionController().apply {
        open(AuthSession.from(MockTokenPolicy.tokens("ana@claramente.local", System.currentTimeMillis())))
    }

    private fun glb(): ByteArray {
        val json = "{\"asset\":{\"version\":\"2.0\"}}  ".toByteArray()
        val total = 20 + json.size
        return ByteBuffer.allocate(total).order(ByteOrder.LITTLE_ENDIAN)
            .putInt(0x46546C67).putInt(2).putInt(total).putInt(json.size).putInt(0x4E4F534A).put(json)
            .array()
    }

    private val model = Model3D("m1", "Célula", "Biologia", null, 20, "abc", "Carla", emptyList())

    @Test
    fun `entrar na turma normaliza o codigo antes de chamar o servidor`() = runBlocking {
        val client = FakeClassroomClient()
        val result = JoinClassroomUseCase(client, signedIn()).execute(" k7m-2qx ")
        assertTrue(result.isSuccess)
        assertEquals(listOf("join:K7M2QX"), client.calls)
    }

    @Test
    fun `codigo invalido falha sem chamar o servidor`() = runBlocking {
        val client = FakeClassroomClient()
        val result = JoinClassroomUseCase(client, signedIn()).execute("123")
        assertEquals(ClassroomErrors.INVALID_CODE, result.exceptionOrNull()?.message)
        assertTrue(client.calls.isEmpty())
    }

    @Test
    fun `modo teste nao chama o servidor`() = runBlocking {
        val client = FakeClassroomClient()
        val result = ListClassroomsUseCase(client, mockSession()).execute()
        assertEquals(ClassroomErrors.MOCK_MODE, result.exceptionOrNull()?.message)
        assertTrue(client.calls.isEmpty())
    }

    @Test
    fun `sem sessao a chamada falha com 401`() = runBlocking {
        val result = ListClassroomsUseCase(FakeClassroomClient(), SessionController()).execute()
        assertEquals(401, (result.exceptionOrNull() as? ApiException)?.status)
    }

    @Test
    fun `preparar modelo baixa uma vez e depois usa o cache`() = runBlocking {
        val client = FakeModelClient(glb())
        val store = FakeModelFileStore()
        val useCase = PrepareModelUseCase(client, store, signedIn())
        val progress = mutableListOf<Float>()
        val first = useCase.execute(model) { progress += it }.getOrThrow()
        val second = useCase.execute(model) { progress += it }.getOrThrow()
        assertEquals(first, second)
        assertEquals(listOf("download:m1"), client.calls)
        assertEquals(2, progress.size)
        assertTrue(progress.first() in 0.4f..0.6f)
        assertEquals(1f, progress.last())
    }

    @Test
    fun `inspecionar aceita glb valido e recusa outros formatos e arquivos grandes`() = runBlocking {
        assertTrue(InspectModelFileUseCase(FakeDocumentReader("celula.glb", glb())).execute("content://x").isSuccess)
        val text = "{\"asset\":{\"version\":\"2.0\"}} gltf de texto".toByteArray()
        assertEquals(ClassroomErrors.NOT_GLB, InspectModelFileUseCase(FakeDocumentReader("celula.gltf", text)).execute("content://x").exceptionOrNull()?.message)
        val huge = FakeDocumentReader("enorme.glb", glb(), reportedSize = ModelFilePolicy.MAX_BYTES + 1)
        assertEquals(ClassroomErrors.FILE_TOO_LARGE, InspectModelFileUseCase(huge).execute("content://x").exceptionOrNull()?.message)
    }

    @Test
    fun `enviar modelo exige titulo e envia o arquivo com nome original`() = runBlocking {
        val client = FakeModelClient()
        val content = glb()
        val useCase = UploadModelUseCase(client, FakeDocumentReader("celula.glb", content), signedIn())
        assertEquals(ClassroomErrors.EMPTY_TITLE, useCase.execute("content://x", " ", null, null).exceptionOrNull()?.message)
        val uploaded = useCase.execute("content://x", "  Célula animal ", " ", null).getOrThrow()
        assertEquals("Célula animal", uploaded.title)
        assertEquals(listOf("upload:Célula animal:null:celula.glb"), client.calls)
        assertArrayEquals(content, client.uploaded)
    }

    @Test
    fun `liberar e retirar modelo usam o endpoint certo`() = runBlocking {
        val client = FakeClassroomClient()
        val useCase = SetModelPublishedUseCase(client, signedIn())
        useCase.execute("c1", "m1", true)
        useCase.execute("c1", "m1", false)
        assertEquals(listOf("publish:c1:m1", "unpublish:c1:m1"), client.calls)
    }

    @Test
    fun `politica de glb confere cabecalho e tamanho declarado`() {
        val content = glb()
        assertTrue(ModelFilePolicy.isGlb(content, content.size.toLong()))
        assertFalse(ModelFilePolicy.isGlb(content, content.size.toLong() + 1))
        assertFalse(ModelFilePolicy.isGlb(content.copyOf(10), content.size.toLong()))
    }

    @Test
    fun `papel admin do banco vira professor e o resto vira aluno`() {
        assertEquals(UserRole.TEACHER, RolePolicy.resolve(listOf(" Admin ")))
        assertEquals(UserRole.STUDENT, RolePolicy.resolve(listOf("student")))
        assertEquals(UserRole.STUDENT, RolePolicy.resolve(emptyList()))
    }

    @Test
    fun `qr do modelo usa o prefixo claramente e volta para o mesmo id`() {
        val id = "3F2B8C1E-9A4D-4E7B-8C21-5D6F7A8B9C0D"
        val content = ModelQrPolicy.content(id)
        assertEquals("claramente:3f2b8c1e-9a4d-4e7b-8c21-5d6f7a8b9c0d", content)
        assertEquals(id.lowercase(), ModelQrPolicy.idFrom(" ${content.uppercase()} "))
        assertEquals("solar", ModelQrPolicy.idFrom("solar"))
    }
}
