package io.samuelagesilas.nbafinals.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import javax.inject.Inject


data class RouteStatusCodes(val fail: Int, val success: Int)

enum class EndpointRouteSecurity {
    UN_SECURED,
    SECURED
}

data class ResolverResponse<T>(val data: T? = null, val status: HttpResponseStatus = OK)

class Responder @Inject constructor(private val router: Router,
                                    private val objectMapper: ObjectMapper,
                                    private val jwtAuthentication: JwtAuthentication) {

    private val logger = LogManager.getLogger(Responder::class.java)

    fun <T> to(path: String,
               endpointRouteSecurity: EndpointRouteSecurity = EndpointRouteSecurity.SECURED,
               x: (ctx: RoutingContext) -> ResolverResponse<T>) {

        if (endpointRouteSecurity == EndpointRouteSecurity.SECURED) {
            router.route(path).blockingHandler(jwtAuthentication::handle)
        }
        router.route(path).blockingHandler { ctx: RoutingContext ->
            try {
                val resolverResponse = x.invoke(ctx)    //invokes the resolver function
                with(ctx.response()) {
                    when (resolverResponse.data != null) {
                        true -> this.setStatusCode(resolverResponse.status).end(objectMapper.writeValueAsString(resolverResponse.data))
                        false -> this.setStatusCode(resolverResponse.status).end()
                    }
                }

            } catch (e: Exception) {
                logger.error(e.message, e)
                var response = ""
                if (e.message != null)
                    response = objectMapper.writeValueAsString(LocalizedErrorResponse(e.message!!))
                if (e is ApiException) {
                    ctx.response().setStatusCode(e.statusCode.code()).end(response)
                } else {
                    ctx.response().setStatusCode(INTERNAL_SERVER_ERROR).end()
                }
            }
        }
    }
}