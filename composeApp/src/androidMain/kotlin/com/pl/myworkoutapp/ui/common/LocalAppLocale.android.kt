package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocale {

    private val LocalAppLocale = staticCompositionLocalOf {
        Locale.getDefault().toLanguageTag()
    }

    actual val current: String
        @Composable
        get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val localeTag = value ?: Locale.getDefault().toLanguageTag()

        Locale.setDefault(Locale.forLanguageTag(localeTag))

        return LocalAppLocale provides localeTag
    }
}