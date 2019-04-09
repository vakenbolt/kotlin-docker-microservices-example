package io.samuelagesilas.nbafinals.core

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