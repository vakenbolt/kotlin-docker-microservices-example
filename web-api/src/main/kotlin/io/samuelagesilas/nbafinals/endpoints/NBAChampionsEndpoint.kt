package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject


class NBAChampionsEndpoint @Inject constructor(resolve: Resolver,
                                               private val nba: NBAChampionsResolver) : Endpoint {

    init {
        resolve.to(Paths.getYears) { nba.selectAllChampionshipYears() }
        resolve.to(Paths.getTeams) { nba.selectChampionshipTeams() }
        resolve.to(Paths.Games.GAMES) { ctx -> nba.selectAllGamesByYear(getYear(ctx), ctx.locale()) }
        resolve.to(Paths.Games.GAME) { ctx ->  nba.selectGameById(getGameId(ctx), ctx.locale()) }
        resolve.to(Paths.Games.WINS) { ctx -> nba.selectAllGamesWonByYear(getYear(ctx), ctx.locale()) }
        resolve.to(Paths.Games.LOSSES) { ctx -> nba.selectAllGamesLostByYear(getYear(ctx), ctx.locale()) }
        resolve.to(Paths.Games.HOME_GAMES) { ctx -> nba.selectAllHomeGamesByYear(getYear(ctx), ctx.locale()) }
        resolve.to(Paths.Games.AWAY_GAMES) { ctx -> nba.selectAllAwayGamesByYear(getYear(ctx), ctx.locale()) }
        resolve.to(Paths.getGamesByTeam) { ctx ->
            val team: String = ctx.getPayload<TeamRequest>().team
            nba.selectAllGamesByTeamName(team)
        }
        resolve.to(Paths.getGamesByTeamAndYear) { ctx ->
            val req = ctx.getPayload<TeamYearRequest>()
            nba.selectAllGamesByYearAndTeamName(req.team, req.year)
        }
    }

    private fun getYear(ctx: RoutingContext): Int {
        return check(ctx.pathParam(PathParameters.YEAR)::toInt) { throw ApiException(BAD_REQUEST) }
    }

    private fun getGameId(ctx: RoutingContext): Int {
        return check(ctx.pathParam(PathParameters.GAME_ID)::toInt) { throw ApiException(BAD_REQUEST) }
    }
}