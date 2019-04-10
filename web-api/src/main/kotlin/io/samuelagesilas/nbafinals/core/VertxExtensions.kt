package io.samuelagesilas.nbafinals.core

import com.fasterxml.jackson.core.type.TypeReference
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import io.samuelagesilas.nbafinals.modules.JacksonModule
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import java.util.*

fun RoutingContext.locale(): Locale {
    val preferredLanguage = this.preferredLanguage()
    return if (preferredLanguage == null) {
        Locale(Locale.ENGLISH.language)
    } else {
        Locale(preferredLanguage.tag())
    }
}

inline fun <reified T>RoutingContext.getPayload(): T {
    val typeReference: TypeReference<T> = object : TypeReference<T>() {}
    check(bodyAsString == null) { throw ApiException(BAD_REQUEST) }
    return JacksonModule.jacksonObjectMapper.readValue<T>(this.bodyAsString, typeReference)
}

fun RoutingContext.fail(statusCode: HttpResponseStatus) = this.fail(statusCode.code())

fun RoutingContext.getTransientUserSubject(statusCode: HttpResponseStatus) : String =  this.get(AUTHENTICATED_JWT_SUBJECT)

fun HttpServerResponse.setStatusCode(statusCode: HttpResponseStatus) = this.setStatusCode(statusCode.code())!!