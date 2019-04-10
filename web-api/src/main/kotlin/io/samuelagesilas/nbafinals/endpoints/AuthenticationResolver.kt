package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.core.ApiException
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import javax.inject.Inject

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val bearer: String)

class AuthenticationResolver @Inject constructor(private val jwtAuthentication: JwtAuthentication) {

    fun authenticate(username: String, password: String): ResolverResponse<AuthenticationResponse> {
        return if (username == "Chicago" && password == "Bulls") {
            ResolverResponse(
                data = AuthenticationResponse(
                    jwtAuthentication.createJwt()
                ), status = HttpResponseStatus.CREATED
            )
        } else {
            throw ApiException(HttpResponseStatus.UNAUTHORIZED)
        }
    }
}