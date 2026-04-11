package com.pl.myworkoutapp.core

import platform.Foundation.NSString
import platform.Foundation.localizedCompare

actual class StringComparator actual constructor() {
    actual fun compare(a: String, b: String): Int {
        return (a as NSString).localizedCompare(b).toInt()
    }
}