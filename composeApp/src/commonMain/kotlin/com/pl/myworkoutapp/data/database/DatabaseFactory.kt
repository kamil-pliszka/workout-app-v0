package com.pl.myworkoutapp.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<WorkoutDatabase>
}