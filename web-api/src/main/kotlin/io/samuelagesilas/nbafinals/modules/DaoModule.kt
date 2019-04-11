package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.dao.UsersDAO
import org.jdbi.v3.core.Jdbi

class DaoModule: AbstractModule() {

    @Provides
    fun providesChampionsDAO(jdbi: Jdbi) : ChampionsDAO = jdbi.onDemand(ChampionsDAO::class.java)


    @Provides
    fun providesUsersDAO(jdbi: Jdbi) : UsersDAO = jdbi.onDemand(UsersDAO::class.java)
}