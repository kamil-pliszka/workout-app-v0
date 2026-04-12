package com.pl.myworkoutapp.data.database

import androidx.room.*

@Database(
    entities = [ExerciseEntity::class, WorkoutPlanEntity::class, WorkoutSessionEntity::class,
        WorkoutEntity::class, WorkoutItemEntity::class],
    version = 1
)
@TypeConverters(
    DbTypeConverters::class
)
@ConstructedBy(WorkoutDatabaseConstructor::class)
abstract class WorkoutDatabase: RoomDatabase() {
    abstract val workoutDao: WorkoutDao

    companion object {
        const val DB_NAME = "workout.db"
    }
}