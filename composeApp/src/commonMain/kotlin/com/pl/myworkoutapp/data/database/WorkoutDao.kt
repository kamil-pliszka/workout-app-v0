package com.pl.myworkoutapp.data.database

import androidx.room.*
import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
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
    suspend fun getExerciseById(id: Long): ExerciseEntity?
    @Query("DELETE FROM ExerciseEntity WHERE id = :id")
    suspend fun deleteExercise(id: Long)

    @Query("SELECT * FROM WorkoutEntity WHERE id = :id")
    suspend fun getWorkoutById(id: Long): WorkoutEntity?
    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity) : Long
    @Update
    suspend fun update(workout: WorkoutEntity): Int
    @Delete
    suspend fun delete(workout: WorkoutEntity): Int

    @Insert
    suspend fun insert(items: List<WorkoutItemEntity>): List<Long>
    @Insert
    suspend fun insert(item: WorkoutItemEntity): Long

    @Query("DELETE FROM WorkoutItemEntity WHERE workoutId = :workoutId")
    suspend fun deleteItemsByWorkoutId(workoutId: Long): Int

    @Query("SELECT * FROM WorkoutItemEntity WHERE workoutId = :workoutId ORDER BY parentId, position")
    suspend fun getSortedItemsByWorkoutId(workoutId: Long): List<WorkoutItemEntity>


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
    suspend fun insertWorkout(customWorkout: WorkoutEntity, items : List<FlatWorkoutItem>) : WorkoutId.Custom {
        val workoutId : Long = insert(customWorkout.copy(updatedAt = currentTimeMilliseconds()))
        saveItems(workoutId, items)
        return workoutId.asWorkoutId()
    }

    @Transaction
    suspend fun updateWorkout(customWorkout: WorkoutEntity, items : List<FlatWorkoutItem>) : WorkoutId.Custom {
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

}