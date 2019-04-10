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
    const val JWT_KEY = "jwt_key"
    const val JWT_EXPIRATION_TIME_SECONDS = "jwt_expiration_time_seconds"
}

enum class Keys {
    NO_RECORDS_FOUND
}

const val AUTHENTICATED_JWT_SUBJECT = "AUTHENTICATED_JWT_SUBJECT"