package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.modules.Endpoint
import javax.inject.Inject

class UserSignUpEndpoint @Inject constructor(respond: Responder,
                                             private val resolver: UserSignUpEndpointResolver) : Endpoint {

    init {
        respond.to(Paths.signUp, EndpointRouteSecurity.UN_SECURED) { ctx ->
            val authenticationRequest = ctx.getPayload<UserSignUpRequest>()
            resolver.signUpUser(authenticationRequest.username, authenticationRequest.password, ctx.locale())
        }
    }
}