package com.pl.myworkoutapp.core

import java.util.Locale

actual fun getSystemLanguage(): String {
    return Locale.getDefault().language
}