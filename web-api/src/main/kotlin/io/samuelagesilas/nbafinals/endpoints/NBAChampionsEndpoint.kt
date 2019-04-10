package io.samuelagesilas.nbafinals.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.dao.ChampionsModel
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject


class NBAChampionsEndpoint @Inject constructor(respond: Responder,
                                               private val nba: NBAChampionsHandlers) : Endpoint {

    init {
        respond.to(Paths.getYears) { nba.selectAllChampionshipYears() }
        respond.to(Paths.getTeams) { nba.selectChampionshipTeams() }
        respond.to(Paths.Games.GAMES) { ctx -> nba.selectAllGamesByYear(ctx) }
        respond.to(Paths.Games.WINS) { ctx -> nba.selectAllGamesWonByYear(ctx) }
        respond.to(Paths.Games.LOSSES) { ctx -> nba.selectAllGamesLostByYear(ctx) }
        respond.to(Paths.Games.HOME_GAMES) { ctx -> nba.selectAllHomeGamesByYear(ctx) }
        respond.to(Paths.Games.AWAY_GAMES) { ctx -> nba.selectAllAwayGamesByYear(ctx) }
        respond.to(Paths.getGamesByTeam) { ctx -> nba.selectAllGamesByTeamName(ctx) }
        respond.to(Paths.getGamesByTeamAndYear) { ctx -> nba.selectAllGamesByYearAndTeamName(ctx) }
    }
}

data class YearsResponse(val years: List<Int>)
data class TeamsResponse(val teams: List<String>)
data class TeamRequest(val team: String)
data class TeamYearRequest(val team: String, val year: Int)

class NBAChampionsHandlers @Inject constructor(private val championsDAO: ChampionsDAO,
                                               private val objectMapper: ObjectMapper,
                                               private val apiException: ApiExceptionFactory) {

    fun selectAllChampionshipYears(): YearsResponse {
        return YearsResponse(years = championsDAO.selectAllChampionshipYears())
    }

    fun selectChampionshipTeams(): TeamsResponse {
        return TeamsResponse(teams = championsDAO.selectChampionshipTeams())
    }

    fun selectAllGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year: Int = ctx.pathParam(PathParameters.YEAR).toInt()
        val result = championsDAO.selectAllGamesByYear(year)
        assert (result.isEmpty()) { throw apiException.new(NOT_FOUND, ctx.locale(), Keys.NO_RECORDS_FOUND) }
        return result
    }

    fun selectAllGamesWonByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year: Int = ctx.pathParam(PathParameters.YEAR).toInt()
        val result = championsDAO.selectAllGamesWonByYear(year)
        assert (result.isEmpty()) { throw apiException.new(NOT_FOUND, ctx.locale(), Keys.NO_RECORDS_FOUND) }
        return result
    }

    fun selectAllGamesLostByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year: Int = ctx.pathParam(PathParameters.YEAR).toInt()
        val result = championsDAO.selectAllGamesLostByYear(year)
        assert (result.isEmpty()) { throw apiException.new(NOT_FOUND, ctx.locale(), Keys.NO_RECORDS_FOUND) }
        return result
    }

    fun selectAllHomeGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year: Int = ctx.pathParam(PathParameters.YEAR).toInt()
        val result = championsDAO.selectAllHomeGamesByYear(year)
        assert (result.isEmpty()) { throw apiException.new(NOT_FOUND, ctx.locale(), Keys.NO_RECORDS_FOUND) }
        return result
    }

    fun selectAllAwayGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year: Int = ctx.pathParam(PathParameters.YEAR).toInt()
        val result = championsDAO.selectAllAwayGamesByYear(year)
        assert (result.isEmpty()) { throw apiException.new(NOT_FOUND, ctx.locale(), Keys.NO_RECORDS_FOUND) }
        return result
    }

    fun selectAllGamesByTeamName(ctx: RoutingContext): List<ChampionsModel> {
        val req: TeamRequest = objectMapper.readValue(ctx.bodyAsString, TeamRequest::class.java)
        return championsDAO.selectAllGamesByTeamName(req.team)
    }

    fun selectAllGamesByYearAndTeamName(ctx: RoutingContext): List<ChampionsModel> {
        val req: TeamYearRequest = objectMapper.readValue(ctx.bodyAsString, TeamYearRequest::class.java)
        return championsDAO.selectAllGamesByTeamNameAndYear(req.team, req.year)
    }
}