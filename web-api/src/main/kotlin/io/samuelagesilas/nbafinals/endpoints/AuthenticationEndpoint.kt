package io.samuelagesilas.nbafinals.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.core.EndpointRouteSecurity.UN_SECURED
import io.samuelagesilas.nbafinals.modules.Endpoint
import javax.inject.Inject

class AuthenticationEndpoint @Inject constructor(respond: Resolver,
                                                 private val objectMapper: ObjectMapper,
                                                 private val auth: AuthenticationResolver): Endpoint {
    init {
        respond.to(Paths.authenticate, UN_SECURED) { ctx ->
            val req = ctx.getPayload<AuthenticationRequest>()
            auth.authenticate(req.username, req.password)
        }
    }
}

