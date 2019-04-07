package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import org.jdbi.v3.core.Jdbi

class DaoModule: AbstractModule() {

    @Provides
    fun providesChampionsDAO(jdbi: Jdbi) : ChampionsDAO = jdbi.onDemand(ChampionsDAO::class.java)
}