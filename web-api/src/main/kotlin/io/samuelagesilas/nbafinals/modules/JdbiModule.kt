package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import javax.sql.DataSource

class JdbiModule: AbstractModule() {

    init {
    }

    @Provides
    fun providesJdbi(dataSource: DataSource): Jdbi {
        val jdbi = Jdbi.create(dataSource)
        jdbi.installPlugin(SqlObjectPlugin())
        return jdbi
    }
}