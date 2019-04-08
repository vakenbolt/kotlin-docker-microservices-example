package io.samuelagesilas.nbafinals.core

object Paths {

    object Games {
        const val GAMES: String = "/games/:year"
        const val WINS: String = "/games/:year/wins"
        const val LOSSES: String = "/games/:year/losses"
        const val HOME_GAMES: String = "/games/:year/home"
        const val AWAY_GAMES: String = "/games/:year/away"
    }

    const val healthCheck: String = "/healthCheck"
    const val getYears: String = "/getYears"
    const val getTeams: String = "/getTeams"
    const val getGamesByTeam: String = "/getGamesByTeam"
    const val getGamesByTeamAndYear: String = "/getGamesByTeamAndYear"
}

object PathParameters {
    const val YEAR = "year"
}