package io.samuelagesilas.nbafinals.core

object ServerConfigPropertyKeys {
    const val PORT = "port"
    const val MYSQL_CACHE_PREP_STATEMENTS = "cachePrepStmts"
    const val MYSQL_VERIFY_SERVER_CERTIFICATE = "verifyServerCertificate"
    const val MYSQL_USE_SSL = "useSSL"
    const val MYSQL_PORT = "mysql_port"
    const val MYSQL_HOST = "mysql_host"
    const val MYSQL_USERNAME = "db_username"
    const val MYSQL_PASSWORD = "db_password"
    const val MYSQL_DATABASE = "db_database"
}

object EndpointRoutingPaths {
    const val HEALTH_CHECK: String = "/health_check"
    const val YEARS: String = "/years"

    object Teams {
        const val TEAMS: String = "/teams"
        const val GAMES: String = "/games/:year"
        const val WINS: String = "/games/:year/wins"
        const val LOSSES: String = "/games/:year/losses"
        const val HOME_GAMES: String = "/games/:year/home"
        const val AWAY_GAMES: String = "/games/:year/away"
    }
}

object PathParameters {
    const val YEAR = "year"
}