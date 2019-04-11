package io.samuelagesilas.nbafinals.endpoints

import com.lambdaworks.crypto.SCryptUtil
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.core.check
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import javax.inject.Inject

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val bearer: String)

class AuthenticationResolver @Inject constructor(private val usersDAO: UsersDAO,
                                                 private val jwtAuthentication: JwtAuthentication,
                                                 private val apiException: ApiExceptionFactory) {

    fun authenticate(username: String, password: String): ResolverResponse<AuthenticationResponse> {
        val hashedPassword = usersDAO.authenticateUser(username)
        check(hashedPassword == null) { throw apiException.create(UNAUTHORIZED) }
        check(!SCryptUtil.check(password, hashedPassword)) { throw apiException.create(UNAUTHORIZED) }
        val jwt = jwtAuthentication.createJwt()
        jwtAuthentication.whiteListToken(jwt.token, jwt.transientUserSubject)
        return ResolverResponse(data = AuthenticationResponse(jwt.token), status = HttpResponseStatus.CREATED)
    }
}