package com.pl.myworkoutapp.core

actual object Log {
    actual inline fun e(tag: String?, message: String, throwable: Throwable?) {
        if (throwable != null) {
            android.util.Log.e(tag, message, throwable)
        } else {
            android.util.Log.e(tag, message)
        }
    }

    actual inline fun d(tag: String?, message: String) {
        android.util.Log.d(tag, message)
    }

    actual inline fun i(tag: String?, message: String) {
        android.util.Log.i(tag, message)
    }
}