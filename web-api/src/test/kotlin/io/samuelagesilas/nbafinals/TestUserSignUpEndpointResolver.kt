package io.samuelagesilas.nbafinals

import com.nhaarman.mockito_kotlin.*
import io.samuelagesilas.nbafinals.core.ApiException
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.dao.User
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.endpoints.UserSignUpEndpointResolver
import io.samuelagesilas.nbafinals.modules.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

class TestUserSignUpEndpointResolver {
    private val username = "TestUser"
    private val password = "TestPassword123"
    private val usersDAO = mock<UsersDAO>() {
        on { selectUserByUsername(any()) } doReturn (User(id = 1,
                                                          passwordHash = SCryptPasswordHasher().hashPassword(password)))
        on { insertUser(eq(username), any()) } doReturn 1
    }
    private val passwordHasher = mock<PasswordHasher> {
        on { hashPassword(password) } doReturn SCryptPasswordHasher().hashPassword(password)
        on { verifyPasswordHash(any(), any()) } doReturn true
    }
    private val jwtAuthentication = mock<AuthenticationHandler>() {
        on { createJwt(any()) } doReturn (Jwt(token = "aaa.bbb.ccc", userId = 1))
    }
    private val apiExceptionFactory = ApiExceptionFactory(LocalizationManager())
    private val locale = Locale.ENGLISH

    @Test
    fun `test user sign up`() {
        val resolver = UserSignUpEndpointResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
        val response = resolver.signUpUser(username, password, locale)
        verify(usersDAO, times(1)).insertUser(eq(username), any())
        verify(usersDAO, times(1)).selectUserByUsername(username)
        verify(jwtAuthentication, times(1)).createJwt(userId = 1)
        verify(jwtAuthentication, times(1)).whiteListToken(any())
        assertEquals("aaa.bbb.ccc", response.data!!.bearer)
        assertEquals(201, response.status.code())
    }

    @Test
    fun `test user sign up if user already exists`() {
        val usersDAO = mock<UsersDAO>() {
            on { insertUser(eq(username), any()) } doThrow SQLIntegrityConstraintViolationException()
        }
        val resolver = UserSignUpEndpointResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.signUpUser(username, password, locale)
        }
        verify(usersDAO, times(1)).insertUser(eq(username), any())
        assertEquals("The username is not available.", err.message)
        assertEquals(409, err.statusCode.code())
    }

    @Test
    fun `test user sign up if database update fails`() {
        val usersDAO = mock<UsersDAO>() {
            on { insertUser(eq(username), any()) } doReturn  0
        }
        val resolver = UserSignUpEndpointResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
        assertThrows<IllegalStateException> {
            resolver.signUpUser(username, password, locale)
        }
        verify(usersDAO, times(1)).insertUser(eq(username), any())
        verify(usersDAO, times(1)).selectUserByUsername(eq(username))
    }

    @Test
    fun `test user sign up if user was not properly inserted into database`() {
        doReturn(null).`when`(usersDAO).selectUserByUsername(eq(username))
        val resolver = UserSignUpEndpointResolver(usersDAO, passwordHasher, jwtAuthentication, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.signUpUser(username, password, locale)
        }
        verify(usersDAO, times(1)).insertUser(eq(username), any())
        verify(usersDAO, times(1)).selectUserByUsername(eq(username))
        assertEquals(500, err.statusCode.code())
        assertNull(err.message)
    }
}