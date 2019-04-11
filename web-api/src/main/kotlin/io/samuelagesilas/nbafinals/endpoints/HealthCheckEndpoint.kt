package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.Router
import javax.inject.Inject


class HealthCheckEndpoint @Inject constructor(router: Router,
                                              resolver: HealthCheckResolver) : Endpoint {

    private val mysqlRoute = router.route(Paths.healthCheck)

    init {
        mysqlRoute.blockingHandler { ctx ->
            val response = ctx.response()
            arrayOf(resolver.mySqlHealthCheck(),
                    resolver.redisHealthCheck())
                    .let { results ->
                        when (results.contains(false)) {
                            true -> response.setStatusCode(500).end()
                            false -> response.setStatusCode(200).end()
                        }
                    }
        }
    }
}