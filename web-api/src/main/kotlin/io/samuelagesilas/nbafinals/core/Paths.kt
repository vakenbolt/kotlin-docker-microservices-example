package io.samuelagesilas.nbafinals.core

import io.samuelagesilas.nbafinals.core.PathParameters.YEAR
import io.samuelagesilas.nbafinals.core.PathParameters.GAME_ID

object Paths {

    object Games {
        const val GAMES: String = "/games/:${YEAR}"
        const val GAME: String = "/games/game/:${GAME_ID}"
        const val WINS: String = "/games/:${YEAR}/wins"
        const val LOSSES: String = "/games/:${YEAR}/losses"
        const val HOME_GAMES: String = "/games/:${YEAR}/home"
        const val AWAY_GAMES: String = "/games/:${YEAR}/away"
    }

    const val healthCheck: String = "/healthCheck"
    const val authenticate: String = "/authenticate"
    const val getYears: String = "/getYears"
    const val getTeams: String = "/getTeams"
    const val getGamesByTeam: String = "/getGamesByTeam"
    const val getGamesByTeamAndYear: String = "/getGamesByTeamAndYear"
}

object PathParameters {
    const val YEAR = "year"
    const val GAME_ID = "game_id"
}