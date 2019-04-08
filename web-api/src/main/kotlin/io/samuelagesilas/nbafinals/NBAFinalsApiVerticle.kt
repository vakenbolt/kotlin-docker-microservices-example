package io.samuelagesilas.nbafinals

import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys
import io.vertx.core.*
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.apache.logging.log4j.LogManager
import javax.inject.Inject
import javax.inject.Named

class NBAFinalsApiVerticle @Inject constructor(@Named(ServerConfigPropertyKeys.PORT) private val port: Int,
                                               private val httpServer: HttpServer,
                                               private val httpRouter: Router) : AbstractVerticle() {

    private lateinit var server: HttpServer
    private var deploymentId: String? = null
    private var logger = LogManager.getLogger()

    override fun start(startFuture: Future<Void>?) {
        this.httpServer
                .requestHandler(this.httpRouter)
                .listen(port) { res: AsyncResult<HttpServer> ->
                    when (res.succeeded()) {
                        true -> {
                            logger.info("HTTPServer Verticle: $deploymentId successfully deployed")
                            startFuture!!.complete()
                        }
                        false -> {
                            logger.error("ERROR starting HTTPServer Verticle: $deploymentId")
                            startFuture!!.fail(res.cause())
                        }
                    }
                }
        this.httpRouter.route("/").blockingHandler { ctx -> ctx.response().end() }
    }

    override fun stop(stopFuture: Future<Void>?) {
        logger.info("Stopping HTTPServer on Verticle: ${deploymentID()}")
        stopFuture!!.complete()
    }

    override fun deploymentID(): String = this.deploymentId!!

    override fun init(vertx: Vertx?, context: Context?) {
        logger.info("Initializing Verticle: ${context!!.deploymentID()}")
        this.deploymentId = context.deploymentID()
    }
}