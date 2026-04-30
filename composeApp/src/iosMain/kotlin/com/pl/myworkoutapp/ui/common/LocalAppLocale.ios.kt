package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.*
import platform.Foundation.*

fun getDefaultLocale(): String {
    return (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"
}

actual object LocalAppLocale {

    private val defaultLocale = getDefaultLocale()

    private val LocalAppLocale = staticCompositionLocalOf { defaultLocale }
    actual val current: String
        @Composable
        get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = value ?: defaultLocale
        if (value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLanguages")
        } else {
            NSUserDefaults.standardUserDefaults.setObject(
                listOf(newLocale),
                "AppleLanguages"
            )
        }

        return LocalAppLocale provides newLocale
    }
}