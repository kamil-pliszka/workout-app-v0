package com.pl.myworkoutapp.core

import platform.Foundation.*

actual fun getSystemLanguage(): String {
    return NSLocale.currentLocale.languageCode.lowercase().take(2)// ?: "en"
}