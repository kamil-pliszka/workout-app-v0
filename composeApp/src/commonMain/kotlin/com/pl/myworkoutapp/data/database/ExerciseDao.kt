package com.pl.myworkoutapp.data.database

import androidx.room.*
import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Upsert
    suspend fun upsert(entity: CustomExerciseEntity)

    @Query("SELECT * FROM CustomExerciseEntity")
    fun observeExercises(): Flow<List<CustomExerciseEntity>>

    @Query("SELECT * FROM CustomExerciseEntity")
    suspend fun getAllExercises(): List<CustomExerciseEntity>

    @Query("SELECT * FROM CustomExerciseEntity WHERE id = :id")
    suspend fun getExerciseById(id: Long): CustomExerciseEntity?

    @Query("DELETE FROM CustomExerciseEntity WHERE id = :id")
    suspend fun deleteExerciseById(id: Long)

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: CustomExerciseEntity): Long

    @Update
    suspend fun update(exercise: CustomExerciseEntity): Int

    @Delete
    suspend fun delete(exercise: CustomExerciseEntity): Int


    suspend fun insertExercise(customExercise: CustomExerciseEntity): ExerciseId.Custom {
        val exerciseId: Long = insert(customExercise.copy(updatedAt = currentTimeMilliseconds()))
        return exerciseId.asExerciseId()
    }

    suspend fun updateExercise(customExercise: CustomExerciseEntity): ExerciseId.Custom {
        val updated = update(customExercise.copy(updatedAt = currentTimeMilliseconds()))
        require(updated > 0) {
            "Exercise not found for update: ${customExercise.id}"
        }
        return customExercise.id.asExerciseId()
    }

}