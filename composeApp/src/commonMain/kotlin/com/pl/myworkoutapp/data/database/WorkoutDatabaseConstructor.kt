package com.pl.myworkoutapp.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object WorkoutDatabaseConstructor: RoomDatabaseConstructor<WorkoutDatabase> {
    override fun initialize(): WorkoutDatabase
}