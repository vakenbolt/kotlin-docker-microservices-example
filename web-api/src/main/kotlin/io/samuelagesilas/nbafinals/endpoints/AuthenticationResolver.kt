package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.core.check
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.modules.AuthenticationHandler
import io.samuelagesilas.nbafinals.modules.PasswordHasher
import javax.inject.Inject

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val bearer: String)

class AuthenticationResolver @Inject constructor(private val usersDAO: UsersDAO,
                                                 private val hasher: PasswordHasher,
                                                 private val jwtAuthentication: AuthenticationHandler,
                                                 private val apiException: ApiExceptionFactory) {

    fun authenticate(username: String, password: String): ResolverResponse<AuthenticationResponse> {
        val userHashedPassword = usersDAO.selectUserByUsername(username)
        check(userHashedPassword == null) { throw apiException.create(UNAUTHORIZED) }
        check(!hasher.verifyPasswordHash(password, userHashedPassword!!.passwordHash)) {
            throw apiException.create(UNAUTHORIZED)
        }
        val jwt = jwtAuthentication.createJwt(userHashedPassword.id)
        jwtAuthentication.whiteListToken(jwt)
        return ResolverResponse(data = AuthenticationResponse(jwt.token), status = HttpResponseStatus.CREATED)
    }
}