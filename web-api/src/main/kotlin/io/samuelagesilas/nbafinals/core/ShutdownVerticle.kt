package io.samuelagesilas.nbafinals.core

import io.samuelagesilas.nbafinals.NBAFinalsApiVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import org.apache.logging.log4j.LogManager

class ShutdownVerticle(private val verticle: NBAFinalsApiVerticle,
                       private val vertx: Vertx) : Thread("main-shutdown-hook") {
    private val logger = LogManager.getLogger(ShutdownVerticle::class)

    override fun run() {
        logger.info("Interrupt signal detected ...")
        try {
            val deploymentId = verticle.deploymentID()
            vertx.undeploy(deploymentId) { result: AsyncResult<Void> ->
                when (result.succeeded()) {
                    true -> logger.info("Verticle: $deploymentId successfully un-deployed")
                    false -> logger.error("ERROR un-deploying Verticle: $deploymentId", result.cause())
                }
            }
        } catch (e: Exception) {
            logger.error("ERROR un-deploying verticle", e)
        }
    }
}