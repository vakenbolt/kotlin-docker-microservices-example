package io.samuelagesilas.nbafinals.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery

public interface ChampionsDAO {
    @SqlQuery("SELECT distinct year FROM nba_finals.champions order by year ASC")
    fun getYears() : List<Int>

    @SqlQuery("SELECT distinct team FROM nba_finals.champions order by team ASC")
    fun getTeams() : List<String>
}
