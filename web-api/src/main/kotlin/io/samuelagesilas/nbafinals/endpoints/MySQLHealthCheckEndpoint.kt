package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.Router
import redis.clients.jedis.Jedis
import javax.inject.Inject
import javax.sql.DataSource


class MySQLHealthCheckEndpoint @Inject constructor(router: Router,
                                                   mySQLHealthCheckResolver: MySQLHealthCheckResolver) : Endpoint {

    private val mysqlRoute = router.route(Paths.healthCheck)

    init {
        mysqlRoute.blockingHandler { ctx ->
            val response = ctx.response()
            arrayOf(mySQLHealthCheckResolver.mySqlHealthCheck(),
                    mySQLHealthCheckResolver.redisHealthCheck())
                    .let { results ->
                        when (results.contains(false)) {
                            true -> response.setStatusCode(500).end()
                            false -> response.setStatusCode(200).end()
                        }
                    }
        }
    }
}


class MySQLHealthCheckResolver @Inject constructor(private val dataSource: DataSource,
                                                   private val redis: Jedis) {

    fun mySqlHealthCheck(): Boolean {
        return try {
            dataSource.connection.use { it.createStatement().use { statement -> statement.execute("SELECT 1") } }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun redisHealthCheck(): Boolean {
        return try {
            redis.use { it.ping().toLowerCase() == "pong" }
        } catch (e: Exception) {
            false
        }
    }
}


