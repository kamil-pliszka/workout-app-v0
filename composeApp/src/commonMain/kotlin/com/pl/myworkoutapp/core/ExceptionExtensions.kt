package com.pl.myworkoutapp.core

fun exceptionToString(e: Throwable) : String {
    return e.message ?: e.cause?.let { it::class.simpleName } ?: ""
}