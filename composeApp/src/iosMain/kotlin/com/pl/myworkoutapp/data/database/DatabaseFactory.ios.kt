@file:OptIn(ExperimentalForeignApi::class)

package com.pl.myworkoutapp.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.pl.myworkoutapp.core.Constants
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<WorkoutDatabase> {
        val dbFile = documentDirectory() + "/${WorkoutDatabase.DB_NAME}"

        if (Constants.DELETE_DB_ON_STARTUP) {
//            if (dbFile.exists()) {
//                println("Deleting $dbFile")
//                dbFile.delete()
//            }
        }

        return Room.databaseBuilder<WorkoutDatabase>(
            name = dbFile
        )
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}