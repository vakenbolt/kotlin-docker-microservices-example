package io.samuelagesilas.nbafinals.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class JacksonModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper().registerModule(KotlinModule())!!
        return objectMapper
    }
}