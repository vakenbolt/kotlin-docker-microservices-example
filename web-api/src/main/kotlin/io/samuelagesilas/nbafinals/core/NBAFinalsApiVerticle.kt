package io.samuelagesilas.nbafinals.core

import io.samuelagesilas.nbafinals.modules.ServerConfig
import io.vertx.core.*
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import org.apache.logging.log4j.LogManager
import javax.inject.Inject

class NBAFinalsApiVerticle @Inject constructor(private val serverConfig: ServerConfig,
                                               private val httpServer: HttpServer,
                                               private val httpRouter: Router) : AbstractVerticle() {

    lateinit var deploymentId: String
    private var logger = LogManager.getLogger()

    override fun start(startFuture: Future<Void>?) {
        this.httpServer
                .requestHandler(this.httpRouter)
                .listen(serverConfig.port) { res: AsyncResult<HttpServer> ->
                    when (res.succeeded()) {
                        true -> {
                            logger.info("HTTPServer Verticle: $deploymentId successfully deployed listening on ${serverConfig.port}")
                            startFuture!!.complete()
                        }
                        false -> {
                            logger.error("ERROR starting HTTPServer Verticle: $deploymentId")
                            startFuture!!.fail(res.cause())
                        }
                    }
                }
        //useful for container environments where the root path is used for health checks
        if (SystemEnvironment.isRootPathAvailable)
            this.httpRouter.route("/").blockingHandler { ctx -> ctx.response().end() }
    }

    override fun stop(stopFuture: Future<Void>?) {
        logger.info("Stopping HTTPServer on Verticle: ${deploymentID()}")
        stopFuture!!.complete()
    }

    override fun deploymentID(): String = this.deploymentId

    override fun init(vertx: Vertx?, context: Context?) {
        logger.info("Initializing Verticle: ${context!!.deploymentID()}")
        this.deploymentId = context.deploymentID()
    }
}