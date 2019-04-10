package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.Multibinder
import io.samuelagesilas.nbafinals.endpoints.AuthenticationEndpoint
import io.samuelagesilas.nbafinals.endpoints.MySQLHealthCheckEndpoint
import io.samuelagesilas.nbafinals.endpoints.NBAChampionsEndpoint

interface Endpoint {}

class EndpointsModule() : AbstractModule() {

    override fun configure() {
        val multiBinder = Multibinder.newSetBinder(binder(), object : TypeLiteral<Endpoint>() {})
        multiBinder.addBinding().to(MySQLHealthCheckEndpoint::class.java)
        multiBinder.addBinding().to(NBAChampionsEndpoint::class.java)
        multiBinder.addBinding().to(AuthenticationEndpoint::class.java)
    }
}