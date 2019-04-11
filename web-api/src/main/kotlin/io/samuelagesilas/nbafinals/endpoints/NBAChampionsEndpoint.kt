package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.modules.Endpoint
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject


class NBAChampionsEndpoint @Inject constructor(respond: Responder,
                                               private val resolver: NBAChampionsResolver) : Endpoint {

    init {
        respond.to(Paths.getYears) { resolver.selectAllChampionshipYears() }
        respond.to(Paths.getTeams) { resolver.selectChampionshipTeams() }
        respond.to(Paths.Games.GAMES) { ctx -> resolver.selectAllGamesByYear(getYear(ctx), ctx.locale()) }
        respond.to(Paths.Games.GAME) { ctx ->  resolver.selectGameById(getGameId(ctx), ctx.locale()) }
        respond.to(Paths.Games.WINS) { ctx -> resolver.selectAllGamesWonByYear(getYear(ctx), ctx.locale()) }
        respond.to(Paths.Games.LOSSES) { ctx -> resolver.selectAllGamesLostByYear(getYear(ctx), ctx.locale()) }
        respond.to(Paths.Games.HOME_GAMES) { ctx -> resolver.selectAllHomeGamesByYear(getYear(ctx), ctx.locale()) }
        respond.to(Paths.Games.AWAY_GAMES) { ctx -> resolver.selectAllAwayGamesByYear(getYear(ctx), ctx.locale()) }
        respond.to(Paths.getGamesByTeam) { ctx ->
            val team: String = ctx.getPayload<TeamRequest>().team
            resolver.selectAllGamesByTeamName(team, ctx.locale())
        }
        respond.to(Paths.getGamesByTeamAndYear) { ctx ->
            val req = ctx.getPayload<TeamYearRequest>()
            resolver.selectAllGamesByYearAndTeamName(req.team, req.year, ctx.locale())
        }
    }

    private fun getYear(ctx: RoutingContext): Int {
        return check(ctx.pathParam(PathParameters.YEAR)::toInt) { throw ApiException(BAD_REQUEST) }
    }

    private fun getGameId(ctx: RoutingContext): Int {
        return check(ctx.pathParam(PathParameters.GAME_ID)::toInt) { throw ApiException(BAD_REQUEST) }
    }
}