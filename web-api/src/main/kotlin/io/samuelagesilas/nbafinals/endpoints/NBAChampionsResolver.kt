package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.Keys
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.dao.ChampionsModel
import java.util.*
import javax.inject.Inject

data class YearsResponse(val years: List<Int>)
data class TeamsResponse(val teams: List<String>)
data class TeamRequest(val team: String)
data class TeamYearRequest(val team: String, val year: Int)


class NBAChampionsResolver @Inject constructor(private val championsDAO: ChampionsDAO,
                                               private val apiException: ApiExceptionFactory) {


    fun selectAllChampionshipYears(): ResolverResponse<YearsResponse> {
        return ResolverResponse(YearsResponse(years = championsDAO.selectAllChampionshipYears()))
    }

    fun selectChampionshipTeams(): ResolverResponse<TeamsResponse> {
        return ResolverResponse(TeamsResponse(teams = championsDAO.selectChampionshipTeams()))
    }

    fun selectAllGamesByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesByYear(year)
        io.samuelagesilas.nbafinals.core.assert(result.isEmpty()) { throw apiException.create(HttpResponseStatus.NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesWonByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesWonByYear(year)
        io.samuelagesilas.nbafinals.core.assert(result.isEmpty()) { throw apiException.create(HttpResponseStatus.NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesLostByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesLostByYear(year)
        io.samuelagesilas.nbafinals.core.assert(result.isEmpty()) { throw apiException.create(HttpResponseStatus.NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllHomeGamesByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllHomeGamesByYear(year)
        io.samuelagesilas.nbafinals.core.assert(result.isEmpty()) { throw apiException.create(HttpResponseStatus.NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllAwayGamesByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllAwayGamesByYear(year)
        io.samuelagesilas.nbafinals.core.assert(result.isEmpty()) { throw apiException.create(HttpResponseStatus.NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesByTeamName(team: String): ResolverResponse<List<ChampionsModel>> {
        return ResolverResponse(championsDAO.selectAllGamesByTeamName(team))
    }

    fun selectAllGamesByYearAndTeamName(team: String, year: Int): ResolverResponse<List<ChampionsModel>> {
        return ResolverResponse(championsDAO.selectAllGamesByTeamNameAndYear(team, year))
    }
}