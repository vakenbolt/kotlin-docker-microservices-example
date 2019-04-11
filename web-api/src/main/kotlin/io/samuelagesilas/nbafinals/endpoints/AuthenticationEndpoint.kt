package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.EndpointRouteSecurity.UN_SECURED
import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.core.Responder
import io.samuelagesilas.nbafinals.core.getPayload
import io.samuelagesilas.nbafinals.modules.Endpoint
import javax.inject.Inject

class AuthenticationEndpoint @Inject constructor(respond: Responder,
                                                 private val resolver: AuthenticationResolver): Endpoint {
    init {
        respond.to(Paths.authenticate, UN_SECURED) { ctx ->
            val req = ctx.getPayload<AuthenticationRequest>()
            resolver.authenticate(req.username, req.password)
        }
    }
}