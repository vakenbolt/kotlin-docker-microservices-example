package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import io.samuelagesilas.nbafinals.core.ApplicationLifeCycleModule

class ApplicationLifeCycleModule : AbstractModule() {

    override fun configure() {
        bind(ApplicationLifeCycleModule::class.java).asEagerSingleton()
    }
}