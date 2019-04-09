package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import java.util.*
import javax.inject.Singleton

class LocalizationModule: AbstractModule() {
    @Provides
    @Singleton
    fun providesResourceBundle(): LocalizationManager = LocalizationManager()
}


class LocalizationManager() {
    companion object ExtendedLocale {
        val SPANISH = Locale("ES")
    }

    private val english = ResourceBundle.getBundle("localized_exception_messages", Locale(Locale.ENGLISH.language))
    private val spanish = ResourceBundle.getBundle("localized_exception_messages", Locale(SPANISH.language))

    fun getBundle(locale: Locale): ResourceBundle? {
        return when (locale.language)  {
            Locale.ENGLISH.language -> english
            SPANISH.language -> spanish
            else -> null
        }
    }
}