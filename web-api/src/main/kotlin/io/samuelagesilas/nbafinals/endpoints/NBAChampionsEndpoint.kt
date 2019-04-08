package io.samuelagesilas.nbafinals.endpoints

import io.samuelagesilas.nbafinals.core.EndpointRoutingPaths
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

    private val yearsRoute = router.route(EndpointRoutingPaths.YEARS)
    private val teamsRoute = router.route(EndpointRoutingPaths.Teams.TEAMS)
    private val gamesRoute = router.route(EndpointRoutingPaths.Teams.GAMES)
    private val gamesWonByYear = router.route(EndpointRoutingPaths.Teams.WINS)
    private val gamesLossesByYear = router.route(EndpointRoutingPaths.Teams.LOSSES)
    private val homeGamesByYear = router.route(EndpointRoutingPaths.Teams.HOME_GAMES)
    private val awayGamesByYear = router.route(EndpointRoutingPaths.Teams.AWAY_GAMES)

    init {
        respond.to(yearsRoute) { nba.selectAllChampionshipYears() }
        respond.to(teamsRoute) { nba.selectChampionshipTeams() }
        respond.to(gamesRoute) { ctx ->  nba.selectAllGamesByYear(ctx) }
        respond.to(gamesWonByYear) { ctx ->  nba.selectAllGamesWonByYear(ctx) }
        respond.to(gamesLossesByYear) { ctx ->  nba.selectAllGamesLostByYear(ctx) }
        respond.to(homeGamesByYear) { ctx ->  nba.selectAllHomeGamesByYear(ctx) }
        respond.to(awayGamesByYear) { ctx ->  nba.selectAllAwayGamesByYear(ctx) }
    }
}

data class YearsResponse(val years:List<Int>)
data class TeamsResponse(val teams:List<String>)

class NBAChampionsHandlers @Inject constructor(private val championsDAO: ChampionsDAO) {

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
}