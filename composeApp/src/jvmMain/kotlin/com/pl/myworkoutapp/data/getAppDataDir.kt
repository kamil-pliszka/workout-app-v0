package com.pl.myworkoutapp.data

import java.io.File

fun getAppDataDir(): File {
    val os = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")
    val appDataDir = when {
        os.contains("win") -> File(System.getenv("APPDATA"), "MyWorkoutApp")
        os.contains("mac") -> File(userHome, "Library/Application Support/MyWorkoutApp")
        else -> File(userHome, ".local/share/MyWorkoutApp")
    }
    return appDataDir
}