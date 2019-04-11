package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import javax.inject.Inject

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val bearer: String)

class AuthenticationResolver @Inject constructor(private val jwtAuthentication: JwtAuthentication,
                                                 private val apiException: ApiExceptionFactory) {

    fun authenticate(username: String, password: String): ResolverResponse<AuthenticationResponse> {
        return if (username == "Chicago" && password == "Bulls") {
            val jwt = jwtAuthentication.createJwt()
            jwtAuthentication.whiteListToken(jwt.token, jwt.transientUserSubject)
            ResolverResponse(data = AuthenticationResponse(jwt.token),
                             status = HttpResponseStatus.CREATED)
        } else {
            throw apiException.create(UNAUTHORIZED)
        }
    }
}