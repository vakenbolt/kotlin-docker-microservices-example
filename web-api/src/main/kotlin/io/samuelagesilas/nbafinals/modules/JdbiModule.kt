package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import javax.sql.DataSource

class JdbiModule: AbstractModule() {

    @Provides
    fun providesJdbi(dataSource: DataSource): Jdbi {
        val jdbi = Jdbi.create(dataSource)
        jdbi.installPlugins()
            .installPlugin(SqlObjectPlugin())
            .installPlugin(KotlinPlugin())
            .installPlugin(KotlinSqlObjectPlugin())
        return jdbi
    }
}