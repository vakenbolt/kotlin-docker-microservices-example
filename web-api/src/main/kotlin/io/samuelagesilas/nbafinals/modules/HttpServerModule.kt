package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.apache.logging.log4j.LogManager
import javax.inject.Singleton


class HttpServerModule : AbstractModule() {

    private val vertx = Vertx.vertx()

    private val logger = LogManager.getLogger(HttpServerModule::class.java)

    @Provides
    @Singleton
    fun providesHttpServer(endpoints: MutableSet<Endpoint>): HttpServer {
        endpoints.forEach { ep: Endpoint -> logger.info("Initialized endpoint: ${ep::class.java.name}.") }
        return vertx.createHttpServer()
    }

    @Provides
    @Singleton
    fun providesHttpRouter(): Router {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        return router
    }


    @Provides
    @com.google.inject.Singleton
    fun providesVertx(): Vertx = Vertx.vertx()
}