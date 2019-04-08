package io.samuelagesilas.nbafinals.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ChampionsDAO {
    @SqlQuery("SELECT distinct year FROM nba_finals.champions order by year ASC")
    fun selectAllChampionshipYears(): List<Int>

    @SqlQuery("SELECT distinct team FROM nba_finals.champions order by team ASC")
    fun selectChampionshipTeams(): List<String>

    @SqlQuery("SELECT * FROM nba_finals.champions where year=:year order by game ASC")
    fun selectAllGamesByYear(@Bind("year") year: Int): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where year=:year AND win=1")
    fun selectAllGamesWonByYear(@Bind("year") year: Int): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where year=:year AND win=0")
    fun selectAllGamesLostByYear(@Bind("year") year: Int): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where year=:year AND home=1")
    fun selectAllHomeGamesByYear(@Bind("year") year: Int): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where year=:year AND home=0")
    fun selectAllAwayGamesByYear(@Bind("year") year: Int): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where team=:team")
    fun selectAllGamesByTeamName(@Bind("team") team: String): List<ChampionsModel>

    @SqlQuery("SELECT * FROM nba_finals.champions where team=:team and year=:year")
    fun selectAllGamesByTeamNameAndYear(@Bind("team") team: String, year: Int): List<ChampionsModel>
}

data class ChampionsModel(val year: Int,
                          val team: String,
                          val game: Int,
                          val win: Boolean,
                          val home: Boolean,
                          val mp: Int,
                          val fg: Int,
                          val fga: Int,
                          val fgp: Double,
                          val tp: Int,
                          val tpa: Int,
                          val tpp: Double,
                          val ft: Int,
                          val fta: Int,
                          val ftp: Double,
                          val orb: Int,
                          val drb: Int,
                          val trb: Int,
                          val ast: Int,
                          val stl: Int,
                          val blk: Int,
                          val tov: Int,
                          val pf: Int,
                          val pts: Int)
