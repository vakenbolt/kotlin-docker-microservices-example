package io.samuelagesilas.nbafinals.core

import org.apache.logging.log4j.LogManager

class SystemEnvironment {

    companion object {
        private val logger = LogManager.getLogger(SystemEnvironment::class.java)

        val isRootPathAvailable: Boolean
            get() {
                val rootPathAvailable: String? = System.getenv(ROOT_PATH_AVAILABLE)
                return rootPathAvailable?.toLowerCase()?.toBoolean() ?: false
            }

        fun getServerConfigPath(): String {
            val serverConfigPath: String? = System.getenv(API_CONFIG)
            if (serverConfigPath == null) {
                logger.error("The environment var: API_CONFIG is not found. Please add to the env with the location " +
                                     "of the configuration file as value. ")
                System.exit(1)
            }
            return serverConfigPath!!
        }
    }
}