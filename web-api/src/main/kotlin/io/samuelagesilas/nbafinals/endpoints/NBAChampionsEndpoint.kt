package io.samuelagesilas.nbafinals.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.core.PathParameters
import io.samuelagesilas.nbafinals.core.Responder
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.dao.ChampionsModel
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject


class NBAChampionsEndpoint @Inject constructor(router: Router,
                                               respond: Responder,
                                               private val nba: NBAChampionsHandlers) : Endpoint {

    private val games = router.route(Paths.Games.GAMES)
    private val gamesWonByYear = router.route(Paths.Games.WINS)
    private val gamesLossesByYear = router.route(Paths.Games.LOSSES)
    private val homeGamesByYear = router.route(Paths.Games.HOME_GAMES)
    private val awayGamesByYear = router.route(Paths.Games.AWAY_GAMES)
    private val getYears = router.route(Paths.getYears)
    private val getTeams = router.route(Paths.getTeams)
    private val getGamesByTeam = router.route(Paths.getGamesByTeam)
    private val getGamesByTeamAndYear = router.route(Paths.getGamesByTeamAndYear)

    init {
        respond.to(getYears) { nba.selectAllChampionshipYears() }
        respond.to(getTeams) { nba.selectChampionshipTeams() }
        respond.to(games) { ctx ->  nba.selectAllGamesByYear(ctx) }
        respond.to(gamesWonByYear) { ctx ->  nba.selectAllGamesWonByYear(ctx) }
        respond.to(gamesLossesByYear) { ctx ->  nba.selectAllGamesLostByYear(ctx) }
        respond.to(homeGamesByYear) { ctx ->  nba.selectAllHomeGamesByYear(ctx) }
        respond.to(awayGamesByYear) { ctx ->  nba.selectAllAwayGamesByYear(ctx) }
        respond.to(getGamesByTeam) { ctx ->  nba.selectAllGamesByTeamName(ctx) }
        respond.to(getGamesByTeamAndYear) { ctx ->  nba.selectAllGamesByYearAndTeamName(ctx) }
    }
}

data class YearsResponse(val years:List<Int>)
data class TeamsResponse(val teams:List<String>)
data class TeamRequest(val team: String)
data class TeamYearRequest(val team: String, val year: Int)

class NBAChampionsHandlers @Inject constructor(private val championsDAO: ChampionsDAO,
                                               private val objectMapper: ObjectMapper) {

    fun selectAllChampionshipYears(): YearsResponse {
        return YearsResponse(years = championsDAO.selectAllChampionshipYears())
    }

    fun selectChampionshipTeams(): TeamsResponse {
        return TeamsResponse(teams = championsDAO.selectChampionshipTeams())
    }

    fun selectAllGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year : Int = ctx.pathParam(PathParameters.YEAR).toInt()
        return championsDAO.selectAllGamesByYear(year)
    }

    fun selectAllGamesWonByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year : Int = ctx.pathParam(PathParameters.YEAR).toInt()
        return championsDAO.selectAllGamesWonByYear(year)
    }

    fun selectAllGamesLostByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year : Int = ctx.pathParam(PathParameters.YEAR).toInt()
        return championsDAO.selectAllGamesLostByYear(year)
    }

    fun selectAllHomeGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year : Int = ctx.pathParam(PathParameters.YEAR).toInt()
        return championsDAO.selectAllHomeGamesByYear(year)
    }

    fun selectAllAwayGamesByYear(ctx: RoutingContext): List<ChampionsModel> {
        val year : Int = ctx.pathParam(PathParameters.YEAR).toInt()
        return championsDAO.selectAllAwayGamesByYear(year)
    }

    fun selectAllGamesByTeamName(ctx: RoutingContext): List<ChampionsModel> {
        val req : TeamRequest = objectMapper.readValue(ctx.bodyAsString, TeamRequest::class.java)
        return championsDAO.selectAllGamesByTeamName(req.team)
    }

    fun selectAllGamesByYearAndTeamName(ctx: RoutingContext): List<ChampionsModel> {
        val req : TeamYearRequest = objectMapper.readValue(ctx.bodyAsString, TeamYearRequest::class.java)
        return championsDAO.selectAllGamesByTeamNameAndYear(req.team, req.year)
    }
}