package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys
import javax.inject.Singleton
import javax.sql.DataSource

class HikariModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesMySqlDataSource(serverConfig: ServerConfig) : DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = String.format("jdbc:mysql://%s:%d/", serverConfig.dbHost, serverConfig.dbPort)
        hikariConfig.username = serverConfig.dbUsername
        hikariConfig.password = serverConfig.dbPassword
        hikariConfig.catalog = serverConfig.useDatabase
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_CACHE_PREP_STATEMENTS, serverConfig.mySqlConfig.cachePrepStmts)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_VERIFY_SERVER_CERTIFICATE, serverConfig.mySqlConfig.verifyServerCertificate)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_USE_SSL, serverConfig.mySqlConfig.useSSL)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_DATABASE, serverConfig.useDatabase)
        return HikariDataSource(hikariConfig)
    }
}