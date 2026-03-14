package com.pl.myworkoutapp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsert(entity: ExerciseEntity)
    @Query("SELECT * FROM ExerciseEntity")
    fun observeExercises(): Flow<List<ExerciseEntity>>
    @Query("SELECT * FROM ExerciseEntity")
    suspend fun getAllExercises(): List<ExerciseEntity>
    @Query("SELECT * FROM ExerciseEntity WHERE id = :id")
    suspend fun getExerciseById(id: Int): ExerciseEntity?
    @Query("DELETE FROM ExerciseEntity WHERE id = :id")
    suspend fun deleteExercise(id: Int)


}