package com.pl.myworkoutapp.core

//https://medium.com/@mohaberabi98/logging-in-compose-multiplatform-a5cf750dbce0
expect object Log {
    fun e(tag: String?, message: String, throwable: Throwable? = null)
    fun d(tag: String?, message: String)
    fun i(tag: String?, message: String)
}