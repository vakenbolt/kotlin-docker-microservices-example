package io.samuelagesilas.nbafinals.core

object ServerConfigPropertyKeys {
    const val MYSQL_CACHE_PREP_STATEMENTS = "cachePrepStmts"
    const val MYSQL_VERIFY_SERVER_CERTIFICATE = "verifyServerCertificate"
    const val MYSQL_USE_SSL = "useSSL"
    const val MYSQL_DATABASE = "useDatabase"
}

enum class Keys {
    NO_RECORDS_FOUND,
    USERNAME_NOT_AVAILABLE,
}

const val AUTHENTICATED_JWT_SUBJECT = "AUTHENTICATED_JWT_SUBJECT"
const val ROOT_PATH_AVAILABLE = "ROOT_PATH_AVAILABLE"
const val API_CONFIG = "API_CONFIG"