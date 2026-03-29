package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.pl.myworkoutapp.core.getSystemLanguage
import java.util.Locale

actual object LocalAppLocale {

    private val defaultLocale: String = getSystemLanguage()//Locale.getDefault().toLanguageTag()

    private val LocalAppLocale = staticCompositionLocalOf { defaultLocale }

    actual val current: String
        @Composable
        get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocaleTag = value ?: defaultLocale
        val locale = Locale.forLanguageTag(newLocaleTag)

        // ustawienie globalnego locale (ważne np. dla formatterów dat/liczb)
        Locale.setDefault(locale)

        return LocalAppLocale provides newLocaleTag
    }
}