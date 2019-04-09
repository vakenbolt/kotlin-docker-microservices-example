package io.samuelagesilas.nbafinals.core

import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.LocalizationManager
import java.util.*
import javax.inject.Inject

data class LocalizedErrorResponse(val errorMessage: String)

class ApiException(val statusCode: Int, localizedMessage: String? = null) : Exception(localizedMessage)

class ApiExceptionFactory @Inject constructor(private val localizationManager: LocalizationManager) {

    fun new(statusCode: HttpResponseStatus, locale: Locale, localizedMessageKey: Keys? = null): ApiException {
        val localizedMessage: String? = if (localizedMessageKey != null) {
            localizationManager.getBundle(locale)?.getString(localizedMessageKey.name)
        } else {
            null
        }
        return ApiException(statusCode.code(), localizedMessage)
    }
}