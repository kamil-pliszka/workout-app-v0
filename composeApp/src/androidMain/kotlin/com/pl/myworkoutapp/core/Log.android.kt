package com.pl.myworkoutapp.core

actual object Log {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            android.util.Log.e(tag, message, throwable)
        } else {
            android.util.Log.e(tag, message)
        }
    }

    actual fun d(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }

    actual fun i(tag: String, message: String) {
        android.util.Log.i(tag, message)
    }
}