package io.samuelagesilas.nbafinals

import com.google.inject.Guice
import io.samuelagesilas.nbafinals.core.NBAFinalsApiVerticle
import io.samuelagesilas.nbafinals.core.ShutdownVerticle
import io.samuelagesilas.nbafinals.modules.*
import io.vertx.core.Vertx


fun main() {
    val serverConfigPath = System.getenv("API_CONFIG")
    val injector = Guice.createInjector(DaoModule(),
                                        EndpointsModule(),
                                        HikariModule(),
                                        HttpServerModule(),
                                        LocalizationModule(),
                                        JacksonModule(),
                                        JdbiModule(),
                                        JwtModule(),
                                        ServerConfigModule(serverConfigPath))
    val nbaFinalsApiServer = injector.getInstance(NBAFinalsApiVerticle::class.java)
    with(Vertx.vertx()) {
        Runtime.getRuntime().addShutdownHook(ShutdownVerticle(nbaFinalsApiServer, this))
        this.deployVerticle(nbaFinalsApiServer)
    }
}