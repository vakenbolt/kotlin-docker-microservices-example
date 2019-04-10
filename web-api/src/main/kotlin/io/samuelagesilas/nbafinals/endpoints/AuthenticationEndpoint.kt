package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.CREATED
import io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.core.EndpointRouteSecurity.UN_SECURED
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val bearer: String)

class AuthenticationEndpoint @Inject constructor(respond: Resolver,
                                                 private val auth: AuthenticationHandlers): Endpoint {
    init {
        respond.to(Paths.authenticate, UN_SECURED) { ctx -> auth.authenticate(ctx) }
    }
}

class AuthenticationHandlers @Inject constructor(private val jwtAuthentication: JwtAuthentication) {

    fun authenticate(ctx: RoutingContext): ResolverResponse<AuthenticationResponse> {
        val authenticationRequest = ctx.getPayload<AuthenticationRequest>()
        return if (authenticationRequest.username == "Chicago" && authenticationRequest.password == "Bulls") {
            ResolverResponse(data = AuthenticationResponse(jwtAuthentication.createJwt()), status = CREATED)
        } else {
            throw ApiException(UNAUTHORIZED)
        }
    }
}