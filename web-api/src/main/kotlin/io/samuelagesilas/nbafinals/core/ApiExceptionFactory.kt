package io.samuelagesilas.nbafinals.core

import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.modules.LocalizationManager
import java.util.*
import javax.inject.Inject

data class LocalizedErrorResponse(val errorMessage: String)

class ApiException(val statusCode: HttpResponseStatus, localizedMessage: String? = null) : Exception(localizedMessage)

class ApiExceptionFactory @Inject constructor(private val localizationManager: LocalizationManager) {

    fun new(statusCode: HttpResponseStatus, locale: Locale, localizedMessageKey: Keys? = null): ApiException {
        val localizedMessage: String? = if (localizedMessageKey != null) {
            localizationManager.getBundle(locale)?.getString(localizedMessageKey.name)
        } else {
            null
        }
        return ApiException(statusCode, localizedMessage)
    }
}