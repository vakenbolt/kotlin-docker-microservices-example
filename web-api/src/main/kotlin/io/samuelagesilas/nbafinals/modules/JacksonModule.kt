package io.samuelagesilas.nbafinals.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class JacksonModule: AbstractModule() {

    companion object {
        val jacksonObjectMapper = jacksonObjectMapper()
    }

    @Provides
    @Singleton
    fun providesObjectMapper(): ObjectMapper = jacksonObjectMapper
}