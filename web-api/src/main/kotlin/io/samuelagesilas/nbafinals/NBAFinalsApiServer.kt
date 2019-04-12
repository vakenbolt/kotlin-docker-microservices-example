package io.samuelagesilas.nbafinals

import com.google.inject.Guice
import io.samuelagesilas.nbafinals.core.ApplicationLifeCycle
import io.samuelagesilas.nbafinals.core.SystemEnvironment
import io.samuelagesilas.nbafinals.modules.*


fun main() {
    val injector = Guice.createInjector(DaoModule(),
                                        EndpointsModule(),
                                        HikariModule(),
                                        HttpServerModule(),
                                        JacksonModule(),
                                        JdbiModule(),
                                        JwtModule(),
                                        LocalizationModule(),
                                        RedisModule(),
                                        ServerConfigModule(configFileLocation = SystemEnvironment.getServerConfigPath()))

    val app = injector.getInstance(ApplicationLifeCycle::class.java)
    Runtime.getRuntime().addShutdownHook(app.shutdownHook)
    app.vertx.deployVerticle(app.verticle)
}