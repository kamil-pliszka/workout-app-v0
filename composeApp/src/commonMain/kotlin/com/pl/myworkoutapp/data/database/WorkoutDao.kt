package com.pl.myworkoutapp.data.database

import androidx.room.*
import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM CustomWorkoutEntity WHERE id = :id")
    suspend fun getWorkoutById(id: Long): CustomWorkoutEntity?

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: CustomWorkoutEntity): Long

    @Update
    suspend fun update(workout: CustomWorkoutEntity): Int

    @Delete
    suspend fun delete(workout: CustomWorkoutEntity): Int

    @Query("DELETE FROM CustomWorkoutEntity WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Insert
    suspend fun insert(items: List<CustomWorkoutItemEntity>): List<Long>

    @Insert
    suspend fun insert(item: CustomWorkoutItemEntity): Long

    @Query("DELETE FROM CustomWorkoutItemEntity WHERE workoutId = :workoutId")
    suspend fun deleteItemsByWorkoutId(workoutId: Long): Int

    @Query("SELECT * FROM CustomWorkoutItemEntity WHERE workoutId = :workoutId ORDER BY parentId, position")
    suspend fun getSortedItemsByWorkoutId(workoutId: Long): List<CustomWorkoutItemEntity>

    private suspend fun saveItems(workoutId: Long, items: List<FlatWorkoutItem>) {
        val itemsId = mutableListOf<Long>()
        items.forEachIndexed { index, item ->
            val id: Long = insert(
                item.itemEntity.copy(
                    workoutId = workoutId,
                    //parentId = item.parentIndex?.let { idx -> itemsId[idx]}, // null = root
                    parentId = item.parentIndex?.let { idx ->
                        require(idx < itemsId.size) {
                            "Invalid parentIndex=$idx at index=$index"
                        }
                        itemsId[idx]
                    },
                    position = item.position,   // kolejność w ramach parenta
                )
            )
            itemsId.add(id)
        }
    }

    @Transaction
    suspend fun insertWorkout(
        customWorkout: CustomWorkoutEntity,
        items: List<FlatWorkoutItem>
    ): WorkoutId.Custom {
        val workoutId: Long = insert(customWorkout.copy(updatedAt = currentTimeMilliseconds()))
        saveItems(workoutId, items)
        return workoutId.asWorkoutId()
    }

    @Transaction
    suspend fun updateWorkout(
        customWorkout: CustomWorkoutEntity,
        items: List<FlatWorkoutItem>
    ): WorkoutId.Custom {
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

    @Query(
        """
        SELECT cw.*
        FROM CustomWorkoutEntity cw
        INNER JOIN (
            SELECT basedOn, MAX(updatedAt) AS maxUpdatedAt
            FROM CustomWorkoutEntity
            WHERE basedOn IN (:baseIds)
            GROUP BY basedOn
        ) latest
        ON cw.basedOn = latest.basedOn
        AND cw.updatedAt = latest.maxUpdatedAt
        """
    )
    suspend fun findLatestBasedOnOldVersion(baseIds: Set<String>): List<CustomWorkoutEntity>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
    SELECT *
    FROM (
        SELECT *,
               ROW_NUMBER() OVER (
                   PARTITION BY basedOn
                   ORDER BY updatedAt DESC, id DESC
               ) as rn
        FROM CustomWorkoutEntity
        WHERE basedOn IN (:baseIds) and planId is null
    )
    WHERE rn = 1
    """
    )
    fun observeLatestBasedOn(baseIds: Set<String>): Flow<List<CustomWorkoutEntity>>

    @Query(
        """
    SELECT id
    FROM (
        SELECT id,
               ROW_NUMBER() OVER (
                   PARTITION BY basedOn
                   ORDER BY updatedAt DESC, id DESC
               ) as rn
        FROM CustomWorkoutEntity
        WHERE basedOn IN (:baseIds) and planId is null
    )
    WHERE rn = 1
    """
    )
    fun observeLatestBasedOnIds(baseIds: Set<String>): Flow<List<Long>>

    @Query(
        """
    SELECT *
    FROM CustomWorkoutEntity
    WHERE basedOn IS NULL 
        AND planId IS NULL
    ORDER BY updatedAt DESC, id DESC
    """
    )
    fun observeMainCustomWorkouts(): Flow<List<CustomWorkoutEntity>>

    @Query(
        """
    SELECT id
    FROM CustomWorkoutEntity
    WHERE basedOn IS NULL 
        AND planId IS NULL
    ORDER BY updatedAt DESC, id DESC
    """
    )
    fun observeMainCustomWorkoutsIds(): Flow<List<Long>>

    @Query(
        """
    SELECT *
    FROM CustomWorkoutEntity
    WHERE basedOn = :baseId
      AND planId IS NULL
    ORDER BY updatedAt DESC, id DESC
    LIMIT 1
    """
    )
    suspend fun findLatestBasedOn(baseId: String): CustomWorkoutEntity?
}