package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys
import javax.inject.Named
import javax.inject.Singleton
import javax.sql.DataSource

class HikariModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesMySqlDataSource(@Named(ServerConfigPropertyKeys.MYSQL_HOST) dbHost: String,
                                @Named(ServerConfigPropertyKeys.MYSQL_PORT) dbPort: Int,
                                @Named(ServerConfigPropertyKeys.MYSQL_USERNAME) username: String,
                                @Named(ServerConfigPropertyKeys.MYSQL_PASSWORD) password: String,
                                @Named(ServerConfigPropertyKeys.MYSQL_DATABASE) database: String,
                                @Named(ServerConfigPropertyKeys.MYSQL_CACHE_PREP_STATEMENTS) cachePrepStatements: Boolean,
                                @Named(ServerConfigPropertyKeys.MYSQL_VERIFY_SERVER_CERTIFICATE) verifyCert: Boolean) : DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = String.format("jdbc:mysql://%s:%d/", dbHost, dbPort)
        hikariConfig.username = username
        hikariConfig.password = password
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_CACHE_PREP_STATEMENTS, cachePrepStatements)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_VERIFY_SERVER_CERTIFICATE, verifyCert)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_USE_SSL, false)
        hikariConfig.addDataSourceProperty(ServerConfigPropertyKeys.MYSQL_DATABASE, database)

        return HikariDataSource(hikariConfig)
    }
}