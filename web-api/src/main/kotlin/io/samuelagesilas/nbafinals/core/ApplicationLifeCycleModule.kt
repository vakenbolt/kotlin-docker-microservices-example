package io.samuelagesilas.nbafinals.core

import com.google.inject.Inject
import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.modules.ApiServerShutdown
import io.samuelagesilas.nbafinals.modules.HikariShutdown
import io.samuelagesilas.nbafinals.modules.RedisShutdown
import io.vertx.core.Vertx
import redis.clients.jedis.Jedis

class ApplicationLifeCycleModule @Inject constructor(private val vertx: Vertx,
                                                     private val verticle: NBAFinalsApiVerticle,
                                                     private val dataSource: HikariDataSource,
                                                     private val redis: Jedis) {

    private val shutdownHook = object : Thread() {
        override fun run() {
            with(ApiServerShutdown(verticle, vertx), ::runThreadAndWait)
            with(HikariShutdown(dataSource), ::runThreadAndWait)
            with(RedisShutdown(redis), ::runThreadAndWait)
        }

        private fun runThreadAndWait(thread: Thread) {
            thread.start()
            thread.join(5000)
        }
    }

    fun start() {
        Runtime.getRuntime().addShutdownHook(this.shutdownHook)
        vertx.deployVerticle(verticle)
    }
}

