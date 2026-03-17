package com.pl.myworkoutapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pl.myworkoutapp.core.Constants

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<WorkoutDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WorkoutDatabase.DB_NAME)

        if (Constants.DELETE_DB_ON_STARTUP) {
            if (dbFile.exists()) {
                println("Deleting $dbFile")
                dbFile.delete()
            }
        }

        return Room.databaseBuilder<WorkoutDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}