package io.samuelagesilas.nbafinals.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import javax.inject.Inject


data class RouteStatusCodes(val fail: Int, val success: Int)

class Responder @Inject constructor(private val objectMapper: ObjectMapper) {

    private val logger  = LogManager.getLogger(Responder::class.java)

    fun <T> to(route: Route, statusCodes: RouteStatusCodes = RouteStatusCodes(500, 200), x: (ctx:RoutingContext) -> T) {
        route.blockingHandler { ctx: RoutingContext ->
            try {
                val jsonResponse = objectMapper.writeValueAsString(x.invoke(ctx))
                ctx.response().setStatusCode(statusCodes.success).end(jsonResponse)
            } catch (e: Exception) {
                logger.throwing(e)
                ctx.fail(statusCodes.fail, e)
            }
        }
    }
}