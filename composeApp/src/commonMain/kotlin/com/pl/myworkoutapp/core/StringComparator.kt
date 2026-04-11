package com.pl.myworkoutapp.core

expect class StringComparator() {
    fun compare(a: String, b: String): Int
}