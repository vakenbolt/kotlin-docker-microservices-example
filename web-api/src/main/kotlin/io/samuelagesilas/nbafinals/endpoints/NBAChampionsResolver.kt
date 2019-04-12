package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.models.ChampionsModel
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
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectGameById(id: Int, locale: Locale): ResolverResponse<ChampionsModel> {
        val result = championsDAO.selectGameById(id)
        check(result == null) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesWonByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesWonByYear(year)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesLostByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesLostByYear(year)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllHomeGamesByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllHomeGamesByYear(year)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllAwayGamesByYear(year: Int, locale: Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllAwayGamesByYear(year)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesByTeamName(team: String, locale:Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesByTeamName(team)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }

    fun selectAllGamesByYearAndTeamName(team: String, year: Int, locale:Locale): ResolverResponse<List<ChampionsModel>> {
        val result = championsDAO.selectAllGamesByTeamNameAndYear(team, year)
        check(result.isEmpty()) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        return ResolverResponse(result)
    }
}