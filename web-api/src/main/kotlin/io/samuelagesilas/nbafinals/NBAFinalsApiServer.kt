package io.samuelagesilas.nbafinals

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provides
import io.samuelagesilas.nbafinals.core.NBAFinalsApiVerticle
import io.samuelagesilas.nbafinals.core.ShutdownVerticle
import io.samuelagesilas.nbafinals.modules.*
import io.vertx.core.Vertx
import java.util.*
import javax.inject.Singleton


fun main() {
    val serverConfigPath = System.getenv("API_CONFIG")
    val injector = Guice.createInjector(DaoModule(),
                                        EndpointsModule(),
                                        HikariModule(),
                                        HttpServerModule(),
                                        LocalizationModule(),
                                        JacksonModule(),
                                        JdbiModule(),
                                        ServerConfigModule(serverConfigPath))
    val nbaFinalsApiServer = injector.getInstance(NBAFinalsApiVerticle::class.java)
    with(Vertx.vertx()) {
        Runtime.getRuntime().addShutdownHook(ShutdownVerticle(nbaFinalsApiServer, this))
        this.deployVerticle(nbaFinalsApiServer)
    }
}


class LocalizationModule: AbstractModule() {




    @Provides
    @Singleton
    fun providesResourceBundle(): LocalizationManager = LocalizationManager()
}

class LocalizationManager() {
    companion object ExtendedLocale {
        val SPANISH = Locale("ES")
    }

    private val english = ResourceBundle.getBundle("localized_exception_messages", Locale(Locale.ENGLISH.language))
    private val spanish = ResourceBundle.getBundle("localized_exception_messages", Locale(SPANISH.language))

    fun getBundle(locale: Locale): ResourceBundle? {
        return when (locale.language)  {
            Locale.ENGLISH.language -> english
            SPANISH.language -> spanish
            else -> null
        }
    }
}