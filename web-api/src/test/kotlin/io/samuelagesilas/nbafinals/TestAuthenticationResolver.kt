package io.samuelagesilas.nbafinals

import com.nhaarman.mockito_kotlin.*
import io.samuelagesilas.nbafinals.core.ApiException
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.dao.User
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.endpoints.AuthenticationResolver
import io.samuelagesilas.nbafinals.modules.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestAuthenticationResolver {
    private val username = "TestUser"
    private val password = "TestPassword123"
    private val usersDAO = mock<UsersDAO>() {
        on { selectUserByUsername(any()) } doReturn (User(id = 1,
                                                          passwordHash = SCryptPasswordHasher().hashPassword(password)))
    }
    private val passwordHasher = mock<PasswordHasher> {
        on { hashPassword(password) } doReturn SCryptPasswordHasher().hashPassword(password)
        on { verifyPasswordHash(any(), any()) } doReturn true
    }
    private val jwtAuthentication = mock<AuthenticationHandler>() {
        on { createJwt(any()) } doReturn (Jwt(token = "aaa.bbb.ccc", userId = 1))
    }
    private val apiExceptionFactory = ApiExceptionFactory(LocalizationManager())

    @Test
    fun `test authenticate`() {
        val resolver = AuthenticationResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
        val response = resolver.authenticate(username, password)
        verify(usersDAO, times(1)).selectUserByUsername(username)
        verify(passwordHasher, times(1)).verifyPasswordHash(eq(password), any())
        verify(jwtAuthentication, times(1)).createJwt(1)
        verify(jwtAuthentication, times(1)).whiteListToken(any())
        assertEquals("aaa.bbb.ccc", response.data!!.bearer)
        assertEquals(201, response.status.code())
    }

    @Test
    fun `test authenticate if username does not exist`() {
        val usersDAO = mock<UsersDAO>() {}
        val err = assertThrows<ApiException> {
            val resolver = AuthenticationResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
            resolver.authenticate(username, password)
        }
        assertEquals(401, err.statusCode.code())
        verify(usersDAO, times(1)).selectUserByUsername(username)
    }
}