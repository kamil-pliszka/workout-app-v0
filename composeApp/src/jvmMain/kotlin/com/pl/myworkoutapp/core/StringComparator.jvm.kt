package com.pl.myworkoutapp.core

actual class StringComparator actual constructor() {
    private val collator = java.text.Collator.getInstance()

    actual fun compare(a: String, b: String): Int {
        return collator.compare(a, b)
    }
}