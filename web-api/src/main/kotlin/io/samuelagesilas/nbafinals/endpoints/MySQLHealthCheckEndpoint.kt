package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject
import javax.sql.DataSource


class MySQLHealthCheckEndpoint @Inject constructor(router: Router, dataSource: DataSource) : Endpoint {

    private val mysqlRoute = router.route(Paths.healthCheck)

    init {
        mysqlRoute.blockingHandler { ctx -> healthCheck(ctx, dataSource) }
    }
}


fun healthCheck(routingContext: RoutingContext, dataSource: DataSource) {
    val response: HttpServerResponse = routingContext.response();
    var isHealthy = true
    try {
        dataSource.connection.use { it.createStatement().use { statement -> statement.execute("SELECT 1") } }
    } catch (e: Exception) {
        isHealthy = false
    }
    if (isHealthy)
        response.setStatusCode(200).end()
    else
        response.setStatusCode(500).end()
}