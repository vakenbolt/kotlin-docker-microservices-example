package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.EndpointRoutingPaths
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import javax.inject.Inject
import javax.sql.DataSource

class MySQLHealthCheckEndpoint @Inject constructor(router: Router,
                                                   dataSource: DataSource) : Endpoint {

    init {
        router.route(EndpointRoutingPaths.HealthCheck.MYSQL).blockingHandler { routingContext ->
            val response: HttpServerResponse = routingContext.response();
            try {
                val conn = dataSource.connection
                val statement = conn.createStatement();
                statement.execute("SELECT 1;")
                statement.close()
                conn.close()
                response.setStatusCode(200).end()
            } catch (e: Exception) {
                response.setStatusCode(500).end()
            }
        }
    }
}