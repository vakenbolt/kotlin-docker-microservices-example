package io.samuelagesilas.nbafinals.endpoints

import redis.clients.jedis.Jedis
import javax.inject.Inject
import javax.sql.DataSource

class HealthCheckResolver @Inject constructor(private val dataSource: DataSource,
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