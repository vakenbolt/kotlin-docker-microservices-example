package io.samuelagesilas.nbafinals

import com.google.inject.Guice
import io.samuelagesilas.nbafinals.core.NBAFinalsApiVerticle
import io.samuelagesilas.nbafinals.core.ShutdownVerticle
import io.samuelagesilas.nbafinals.core.SystemEnvironment
import io.samuelagesilas.nbafinals.modules.*
import io.vertx.core.Vertx


fun main() {
    val serverConfigPath = SystemEnvironment.getServerConfigPath();
    val injector = Guice.createInjector(DaoModule(),
                                        EndpointsModule(),
                                        HikariModule(),
                                        HttpServerModule(),
                                        JacksonModule(),
                                        JdbiModule(),
                                        JwtModule(),
                                        LocalizationModule(),
                                        RedisModule(),
                                        ServerConfigModule(serverConfigPath))
    val nbaFinalsApiServer = injector.getInstance(NBAFinalsApiVerticle::class.java)
    with(Vertx.vertx()) {
        Runtime.getRuntime().addShutdownHook(ShutdownVerticle(nbaFinalsApiServer, this))
        this.deployVerticle(nbaFinalsApiServer)
    }
}