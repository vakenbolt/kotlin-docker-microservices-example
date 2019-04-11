package io.samuelagesilas.nbafinals.endpoints

import com.lambdaworks.crypto.SCryptUtil
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.modules.Endpoint
import javax.inject.Inject

class UserSignUpEndpoint @Inject constructor(respond: Responder,
                                             private val resolver: UserSignUpEndpointResolver) : Endpoint {

    init {
        respond.to(Paths.signUp, EndpointRouteSecurity.UN_SECURED) { ctx ->
            val authenticationRequest = ctx.getPayload<UserSignUpRequest>()
            val hashedPassword = SCryptUtil.scrypt(authenticationRequest.password, 16384, 8, 1)
            resolver.signUpUser(authenticationRequest.username, hashedPassword, ctx.locale())
        }
    }
}