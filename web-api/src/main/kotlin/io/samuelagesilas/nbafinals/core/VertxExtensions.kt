package io.samuelagesilas.nbafinals.core

import com.fasterxml.jackson.core.type.TypeReference
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import io.samuelagesilas.nbafinals.modules.JacksonModule
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import java.util.*

fun RoutingContext.locale(): Locale {
    val preferredLanguage = this.preferredLanguage()
    return if (preferredLanguage == null) {
        Locale(Locale.ENGLISH.language)
    } else {
        Locale(preferredLanguage.tag())
    }
}

inline fun <reified T> RoutingContext.getPayload(): T {
    val typeReference: TypeReference<T> = object : TypeReference<T>() {}
    check(bodyAsString == null) { throw ApiException(BAD_REQUEST) }
    val typedPayload = JacksonModule.jacksonObjectMapper.readValue<T>(this.bodyAsString, typeReference)
    val validationResponse = try {
         Validator.validator.validate(typedPayload)
    } catch (e: Exception) {
        LogManager.getLogger().error(e)
        throw ApiException()
    }
    check(validationResponse.size > 0) { throw ApiException(BAD_REQUEST, validationResponse.first().message) }
    return typedPayload
}

fun RoutingContext.fail(statusCode: HttpResponseStatus) = this.fail(statusCode.code())

fun RoutingContext.getTransientUserSubject(): String = this.get(AUTHENTICATED_JWT_SUBJECT)

fun HttpServerResponse.setStatusCode(statusCode: HttpResponseStatus) = this.setStatusCode(statusCode.code())!!