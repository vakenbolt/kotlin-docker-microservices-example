package io.samuelagesilas.nbafinals.modules

import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.core.NBAFinalsApiVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import org.apache.logging.log4j.LogManager
import redis.clients.jedis.Jedis
import java.util.concurrent.CountDownLatch


class ApiServerShutdown(private val verticle: NBAFinalsApiVerticle,
                        private val vertx: Vertx) : Thread("verticle-shutdown-hook") {

    private val logger = LogManager.getLogger(ApiServerShutdown::class.java)

    override fun run() {
        logger.info("Shutting down API Verticle")
        val latch = CountDownLatch(1)
        try {
            val deploymentId = verticle.deploymentID()
            vertx.undeploy(deploymentId) { result: AsyncResult<Void> ->
                when (result.succeeded()) {
                    true -> logger.info("Verticle: $deploymentId successfully un-deployed")
                    false -> logger.error("ERROR un-deploying Verticle: $deploymentId", result.cause())
                }
                latch.countDown()
            }
        } catch (e: Exception) {
            logger.error("ERROR un-deploying verticle", e)
        }
        latch.await()
    }
}


class HikariShutdown(dataSource: HikariDataSource) {

    private val logger = LogManager.getLogger(HikariShutdown::class.java)

    init {
        logger.info("Shutting down Hikari Connection Pool")
        dataSource.close()
    }
}



class RedisShutdown(redis: Jedis) : Thread("redis-shutdown-hook") {

    private val logger = LogManager.getLogger(RedisShutdown::class.java)

    init {
        logger.info("Shutting down Redis")
        redis.close()
    }
}