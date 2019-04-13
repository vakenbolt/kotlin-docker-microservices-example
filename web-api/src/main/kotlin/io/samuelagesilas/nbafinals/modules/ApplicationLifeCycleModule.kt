package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.core.NBAFinalsApiVerticle
import io.vertx.core.Vertx
import redis.clients.jedis.Jedis

class ApplicationLifeCycleModule : AbstractModule() {

    override fun configure() {
        bind(ApplicationLifeCycleModule::class.java).asEagerSingleton()
    }
}


class ApplicationLifeCycle @Inject constructor(private val vertx: Vertx,
                                               private val verticle: NBAFinalsApiVerticle,
                                               private val dataSource: HikariDataSource,
                                               private val redis: Jedis,
                                               private val serverConfig: ServerConfig) {

    private val shutdownHook = object : Thread() {
        override fun run() {
            val t = ApiServerShutdown(verticle, vertx)
            t.start()
            t.join()
            HikariShutdown(dataSource)
            RedisShutdown(redis)
        }

        private fun runAndWait(thread: Thread) {
            thread.start()
            thread.join(serverConfig.shutdownTimeoutSeconds)
        }
    }

    fun start() {
        Runtime.getRuntime().addShutdownHook(this.shutdownHook)
        vertx.deployVerticle(verticle)
    }
}