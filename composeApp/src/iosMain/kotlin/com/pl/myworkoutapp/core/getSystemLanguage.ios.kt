package com.pl.myworkoutapp.core

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getSystemLanguage(): String {
    return NSLocale.currentLocale.languageCode.lowercase().take(2)// ?: "en"
}