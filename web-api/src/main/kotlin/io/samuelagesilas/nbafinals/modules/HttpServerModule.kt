package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import javax.inject.Singleton

class HttpServerModule : AbstractModule() {

    private val vertx = Vertx.vertx()

    @Provides
    @Singleton
    fun providesHttpServer(endpoints: MutableSet<Endpoint>): HttpServer = vertx.createHttpServer()

    @Provides
    @Singleton
    fun providesHttpRouter(): Router = Router.router(vertx)
}