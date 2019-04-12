package io.samuelagesilas.nbafinals.modules

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import java.io.File

class ServerConfigModule constructor(private val configFileLocation: String) : AbstractModule() {

    @Provides
    @Singleton
    fun provides(objectMapper: ObjectMapper): ServerConfig {
        val configFileContent: String = File(configFileLocation).readText()
        val typeReference: TypeReference<ServerConfig> = object : TypeReference<ServerConfig>() {}
        return objectMapper.readValue<ServerConfig>(configFileContent, typeReference)
    }
}

data class ServerConfig(val port: Int,
                        val mySqlConfig: MySqlConfig,
                        val dbPort: Int,
                        val dbHost: String,
                        val dbUsername: String,
                        val dbPassword: String,
                        val useDatabase: String,
                        val jwtKey: String,
                        val jwt_expiration_time_seconds: Int,
                        val redisHost: String,
                        val redisPort: Int)

data class MySqlConfig(val cachePrepStmts: Boolean,
                       val verifyServerCertificate: Boolean,
                       val useSSL: Boolean)