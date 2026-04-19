package com.pl.myworkoutapp.data.database

import androidx.room.*
import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
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

    @Query("SELECT * FROM CustomWorkoutEntity WHERE id = :id")
    suspend fun getWorkoutById(id: Long): CustomWorkoutEntity?
    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: CustomWorkoutEntity) : Long
    @Update
    suspend fun update(workout: CustomWorkoutEntity): Int
    @Delete
    suspend fun delete(workout: CustomWorkoutEntity): Int

    @Insert
    suspend fun insert(items: List<CustomWorkoutItemEntity>): List<Long>
    @Insert
    suspend fun insert(item: CustomWorkoutItemEntity): Long

    @Query("DELETE FROM CustomWorkoutItemEntity WHERE workoutId = :workoutId")
    suspend fun deleteItemsByWorkoutId(workoutId: Long): Int

    @Query("SELECT * FROM CustomWorkoutItemEntity WHERE workoutId = :workoutId ORDER BY parentId, position")
    suspend fun getSortedItemsByWorkoutId(workoutId: Long): List<CustomWorkoutItemEntity>


    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: CustomExerciseEntity) : Long
    @Update
    suspend fun update(exercise: CustomExerciseEntity): Int
    @Delete
    suspend fun delete(exercise: CustomExerciseEntity): Int



    private suspend fun saveItems(workoutId : Long, items : List<FlatWorkoutItem>) {
        val itemsId = mutableListOf<Long>()
        items.forEachIndexed { index, item ->
            val id : Long = insert(item.itemEntity.copy(
                workoutId = workoutId,
                //parentId = item.parentIndex?.let { idx -> itemsId[idx]}, // null = root
                parentId = item.parentIndex?.let { idx ->
                    require(idx < itemsId.size) {
                        "Invalid parentIndex=$idx at index=$index"
                    }
                    itemsId[idx]
                },
                position = item.position,   // kolejność w ramach parenta
            ))
            itemsId.add(id)
        }
    }

    @Transaction
    suspend fun insertWorkout(customWorkout: CustomWorkoutEntity, items : List<FlatWorkoutItem>) : WorkoutId.Custom {
        val workoutId : Long = insert(customWorkout.copy(updatedAt = currentTimeMilliseconds()))
        saveItems(workoutId, items)
        return workoutId.asWorkoutId()
    }

    @Transaction
    suspend fun updateWorkout(customWorkout: CustomWorkoutEntity, items : List<FlatWorkoutItem>) : WorkoutId.Custom {
        //elementy treningu w ramach workout nie mają ID,
        // wiec najpierw usuwamy wszystkie WorkoutItemEntity
        // a potem tworzymy je na nowo
        val updated = update(customWorkout.copy(updatedAt = currentTimeMilliseconds()))
        require(updated > 0) {
            "Workout not found for update: ${customWorkout.id}"
        }
        val deletedCount = deleteItemsByWorkoutId(customWorkout.id)
        println("deletedCount: $deletedCount")
        saveItems(customWorkout.id, items)
        return customWorkout.id.asWorkoutId()
    }

    suspend fun insertExercise(customExercise: CustomExerciseEntity) : ExerciseId.Custom {
        val exerciseId : Long = insert(customExercise.copy(updatedAt = currentTimeMilliseconds()))
        return exerciseId.asExerciseId()
    }

    suspend fun updateExercise(customExercise: CustomExerciseEntity) : ExerciseId.Custom {
        val updated = update(customExercise.copy(updatedAt = currentTimeMilliseconds()))
        require(updated > 0) {
            "Exercise not found for update: ${customExercise.id}"
        }
        return customExercise.id.asExerciseId()
    }

}