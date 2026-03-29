package com.pl.myworkoutapp.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.pl.myworkoutapp.core.Constants
import com.pl.myworkoutapp.data.getAppDataDir
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<WorkoutDatabase> {
        val appDataDir = getAppDataDir()
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