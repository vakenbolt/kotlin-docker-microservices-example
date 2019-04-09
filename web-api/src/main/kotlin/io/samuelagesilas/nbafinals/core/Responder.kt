package io.samuelagesilas.nbafinals.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import javax.inject.Inject


data class RouteStatusCodes(val fail: Int, val success: Int)

class Responder @Inject constructor(private val router: Router,
                                    private val objectMapper: ObjectMapper) {

    private val logger = LogManager.getLogger(Responder::class.java)

    fun <T> to(path: String,
               statusCodes: RouteStatusCodes = RouteStatusCodes(fail = 500, success = 200),
               x: (ctx: RoutingContext) -> T) {

        router.route(path).blockingHandler { ctx: RoutingContext ->
            try {
                val jsonResponse = objectMapper.writeValueAsString(x.invoke(ctx))
                ctx.response().setStatusCode(statusCodes.success).end(jsonResponse)
            } catch (e: Exception) {
                logger.throwing(e)
                val response = objectMapper.writeValueAsString(LocalizedErrorResponse(e.message!!))
                if (e is ApiException) {
                    ctx.response().setStatusCode(e.statusCode).end(response)
                } else {
                    ctx.response().setStatusCode(statusCodes.fail).end(response)
                }
            }
        }
    }
}