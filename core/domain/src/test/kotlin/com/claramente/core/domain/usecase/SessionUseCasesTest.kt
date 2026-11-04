package com.claramente.core.domain.usecase

import com.claramente.core.auth.controller.SessionController
import com.claramente.core.auth.policy.MockTokenPolicy
import com.claramente.core.model.auth.AuthTokens
import com.claramente.core.model.auth.UserRole
import com.claramente.core.network.http.ApiException
import com.claramente.core.network.http.AuthSession
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionUseCasesTest {
    private val undecodable = AuthTokens("not-a-jwt", "stale-refresh", "expires")

    private fun validTokens(email: String = "ana@claramente.local"): AuthTokens =
        MockTokenPolicy.tokens(email, System.currentTimeMillis())

    private fun expiredTokens(): AuthTokens =
        MockTokenPolicy.tokens("ana@claramente.local", System.currentTimeMillis() - EIGHT_DAYS_MS).copy(refreshToken = "old-refresh")

    @Test
    fun `entrar abre sessao com dados do JWT e salva tokens`() = runBlocking {
        val tokens = validTokens("ana@claramente.local")
        val client = FakeAuthClient(AuthSession.from(tokens))
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = LoginUseCase(client, store, sessions).execute("ana@claramente.local", "senha")
        val session = result.getOrThrow()
        assertEquals("ana@claramente.local", session.email)
        assertEquals("Teste Local", session.name)
        assertEquals(listOf("student"), session.roles)
        assertEquals(UserRole.STUDENT, session.role)
        assertEquals(session, sessions.current.value)
        assertEquals(listOf("login:ana@claramente.local"), client.calls)
        assertEquals(listOf("save"), store.calls)
        assertEquals(tokens, store.current())
    }

    @Test
    fun `entrar com token indecodificavel nao persiste tokens e devolve falha`() = runBlocking {
        val client = FakeAuthClient(AuthSession.from(undecodable))
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = LoginUseCase(client, store, sessions).execute("ana@claramente.local", "senha")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiException)
        assertTrue(store.calls.isEmpty())
        assertNull(store.current())
        assertNull(sessions.current.value)
    }

    @Test
    fun `restaurar com token valido abre sessao sem chamar refresh`() = runBlocking {
        val tokens = validTokens()
        val client = FakeAuthClient()
        val store = FakeTokenStore(tokens)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        val session = result.getOrThrow()
        assertNotNull(session)
        assertEquals("ana@claramente.local", session?.email)
        assertEquals(session, sessions.current.value)
        assertTrue(sessions.checked.value)
        assertTrue(client.calls.isEmpty())
        assertEquals(listOf("load"), store.calls)
        assertEquals(tokens, store.current())
    }

    @Test
    fun `restaurar com token expirado renova salva e abre sessao`() = runBlocking {
        val renewed = validTokens("ana@claramente.local")
        val client = FakeAuthClient(AuthSession.from(renewed))
        val store = FakeTokenStore(expiredTokens())
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        val session = result.getOrThrow()
        assertNotNull(session)
        assertEquals("ana@claramente.local", session?.email)
        assertEquals(session, sessions.current.value)
        assertTrue(sessions.checked.value)
        assertEquals(listOf("refresh:old-refresh"), client.calls)
        assertEquals(listOf("load", "save"), store.calls)
        assertEquals(renewed, store.current())
    }

    @Test
    fun `restaurar nao salva quando refresh devolve token indecodificavel`() = runBlocking {
        val expired = expiredTokens()
        val client = FakeAuthClient(AuthSession.from(undecodable))
        val store = FakeTokenStore(expired)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertTrue(result.isFailure)
        assertTrue(sessions.checked.value)
        assertNull(sessions.current.value)
        assertEquals(listOf("refresh:old-refresh"), client.calls)
        assertEquals(listOf("load"), store.calls)
        assertEquals(expired, store.current())
    }

    @Test
    fun `restaurar sem tokens devolve null e marca verificado`() = runBlocking {
        val client = FakeAuthClient()
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertNull(result.getOrThrow())
        assertTrue(sessions.checked.value)
        assertNull(sessions.current.value)
        assertEquals(listOf("load"), store.calls)
        assertTrue(client.calls.isEmpty())
    }

    @Test
    fun `restaurar com token invalido tenta refresh e limpa tokens quando rejeitado`() = runBlocking {
        val client = FakeAuthClient(refreshFailure = ApiException(401, "expirado"))
        val store = FakeTokenStore(undecodable)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertNull(result.getOrThrow())
        assertTrue(sessions.checked.value)
        assertEquals(listOf("refresh:stale-refresh"), client.calls)
        assertEquals(listOf("load", "clear"), store.calls)
        assertNull(store.current())
    }

    @Test
    fun `restaurar limpa tokens tambem quando refresh devolve 403`() = runBlocking {
        val client = FakeAuthClient(refreshFailure = ApiException(403, "proibido"))
        val store = FakeTokenStore(undecodable)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertNull(result.getOrThrow())
        assertTrue(sessions.checked.value)
        assertEquals(listOf("load", "clear"), store.calls)
    }

    @Test
    fun `restaurar preserva tokens quando refresh falha por rede`() = runBlocking {
        val client = FakeAuthClient(refreshFailure = IOException("sem rede"))
        val store = FakeTokenStore(undecodable)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertNull(result.getOrThrow())
        assertTrue(sessions.checked.value)
        assertEquals(listOf("load"), store.calls)
        assertEquals(undecodable, store.current())
    }

    @Test
    fun `restaurar preserva tokens quando refresh falha com erro de servidor`() = runBlocking {
        val client = FakeAuthClient(refreshFailure = ApiException(500, "indisponivel"))
        val store = FakeTokenStore(undecodable)
        val sessions = SessionController()
        val result = RestoreSessionUseCase(client, store, sessions).execute()
        assertNull(result.getOrThrow())
        assertTrue(sessions.checked.value)
        assertEquals(listOf("load"), store.calls)
        assertEquals(undecodable, store.current())
    }

    @Test
    fun `entrar em modo teste abre sessao com o email informado`() = runBlocking {
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = MockLoginUseCase(store, sessions).execute("ana@claramente.local")
        val session = result.getOrThrow()
        assertEquals("ana@claramente.local", session.email)
        assertEquals("Teste Local", session.name)
        assertEquals(session, sessions.current.value)
        assertEquals(listOf("save"), store.calls)
        assertEquals(session.auth.snapshot(), store.current())
    }

    @Test
    fun `entrar em modo teste usa o email padrao quando nenhum e informado`() = runBlocking {
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = MockLoginUseCase(store, sessions).execute(null)
        val session = result.getOrThrow()
        assertEquals("teste@claramente.local", session.email)
        assertEquals(session, sessions.current.value)
        assertEquals(listOf("save"), store.calls)
        assertNotNull(store.current())
    }

    @Test
    fun `sessao comeca nao verificada e sem usuario`() {
        val sessions = SessionController()
        assertFalse(sessions.checked.value)
        assertNull(sessions.current.value)
        assertNull(sessions.auth)
    }

    @Test
    fun `sair com sessao aberta avisa o servidor e limpa tokens e sessao`() = runBlocking {
        val tokens = validTokens()
        val client = FakeAuthClient()
        val store = FakeTokenStore(tokens)
        val sessions = SessionController()
        sessions.open(AuthSession.from(tokens))
        val result = LogoutUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertEquals(listOf("logout"), client.calls)
        assertEquals(listOf("clear"), store.calls)
        assertNull(store.current())
        assertNull(sessions.current.value)
        assertNull(sessions.auth)
    }

    @Test
    fun `sair limpa tokens e sessao mesmo quando o servidor falha`() = runBlocking {
        val tokens = validTokens()
        val client = FakeAuthClient(logoutFailure = IOException("sem rede"))
        val store = FakeTokenStore(tokens)
        val sessions = SessionController()
        sessions.open(AuthSession.from(tokens))
        val result = LogoutUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertEquals(listOf("logout"), client.calls)
        assertEquals(listOf("clear"), store.calls)
        assertNull(store.current())
        assertNull(sessions.current.value)
    }

    @Test
    fun `sair sem sessao limpa tokens e nao chama o servidor`() = runBlocking {
        val client = FakeAuthClient()
        val store = FakeTokenStore(undecodable)
        val sessions = SessionController()
        val result = LogoutUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertTrue(client.calls.isEmpty())
        assertEquals(listOf("clear"), store.calls)
        assertNull(store.current())
        assertNull(sessions.current.value)
    }

    @Test
    fun `sair e sempre sucesso mesmo com store vazio`() = runBlocking {
        val client = FakeAuthClient()
        val store = FakeTokenStore()
        val sessions = SessionController()
        val result = LogoutUseCase(client, store, sessions).execute()
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrThrow())
        assertEquals(listOf("clear"), store.calls)
    }

    private companion object {
        const val EIGHT_DAYS_MS = 8L * 24 * 60 * 60 * 1000
    }
}
