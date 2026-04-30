package com.pl.myworkoutapp.data.database

import androidx.room.*

@Database(
    entities = [CustomExerciseEntity::class, CustomWorkoutPlanEntity::class, WorkoutSessionEntity::class,
        CustomWorkoutEntity::class, CustomWorkoutItemEntity::class],
    version = 1
)
@TypeConverters(
    DbTypeConverters::class
)
@ConstructedBy(WorkoutDatabaseConstructor::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val workoutDao: WorkoutDao

    companion object {
        const val DB_NAME = "workout.db"
    }
}