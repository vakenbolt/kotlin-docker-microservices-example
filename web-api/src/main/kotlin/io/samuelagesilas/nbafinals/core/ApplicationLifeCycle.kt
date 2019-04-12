package io.samuelagesilas.nbafinals.core

import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.modules.ApiServerShutdown
import io.samuelagesilas.nbafinals.modules.HikariShutdown
import io.samuelagesilas.nbafinals.modules.RedisShutdown
import io.vertx.core.Vertx
import redis.clients.jedis.Jedis

class ApplicationLifeCycle(val vertx: Vertx,
                           val verticle: NBAFinalsApiVerticle,
                           val dataSource: HikariDataSource,
                           val redis: Jedis) {

    val shutdownHook = object : Thread() {
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
}

