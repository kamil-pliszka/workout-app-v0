package com.pl.myworkoutapp.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.pl.myworkoutapp.core.Constants
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<WorkoutDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "WorkoutDB")
            os.contains("mac") -> File(userHome, "Library/Application Support/WorkoutDB")
            else -> File(userHome, ".local/share/WorkoutDB")
        }

        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, WorkoutDatabase.DB_NAME)
        if (Constants.DELETE_DB_ON_STARTUP) {
            if (dbFile.exists()) {
                println("Deleting $dbFile")
                dbFile.delete()
            }
        }
        println("dbFile: ${dbFile.absolutePath}")
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}