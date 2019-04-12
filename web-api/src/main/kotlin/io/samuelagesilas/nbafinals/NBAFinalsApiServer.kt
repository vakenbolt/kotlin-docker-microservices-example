package io.samuelagesilas.nbafinals

import com.google.inject.Guice
import io.samuelagesilas.nbafinals.core.ApplicationLifeCycleModule
import io.samuelagesilas.nbafinals.core.SystemEnvironment
import io.samuelagesilas.nbafinals.modules.*


fun main() {
    Guice.createInjector(ApplicationLifeCycleModule(),
                         CryptoModule(),
                         DaoModule(),
                         EndpointsModule(),
                         HikariModule(),
                         HttpServerModule(),
                         JacksonModule(),
                         JdbiModule(),
                         JwtModule(),
                         LocalizationModule(),
                         RedisModule(),
                         ServerConfigModule(configFileLocation = SystemEnvironment.getServerConfigPath()))
            .getInstance(ApplicationLifeCycleModule::class.java)
            .start()
}

