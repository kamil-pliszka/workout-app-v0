package com.pl.myworkoutapp.data.database

import androidx.room.*


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CustomWorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CustomWorkoutItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CustomExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["customExerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("workoutId"),
        Index("parentId"),
        Index("customExerciseId"),
    ]
)
data class CustomWorkoutItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val workoutId: Long,
    val parentId: Long?, // null = root
    val position: Int,   // kolejność w ramach parenta
    val type: String,    // "EXERCISE" | "CIRCUIT"
    // --- Exercise ---
    //val exerciseId: String?,
    val builtInExerciseId: String?,
    val customExerciseId: Long?,
    val quantityValue: Int?,
    val quantityType: String?,
    // --- Circuit ---
    val phase: String?,
    val name: String?,
    val structureType: String?,
    val structureData: String?, // np JSON dla EMOM/AMRAP
)