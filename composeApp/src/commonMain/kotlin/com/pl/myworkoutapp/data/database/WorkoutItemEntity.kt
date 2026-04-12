package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("workoutId"),
        Index("parentId")
    ]
)
data class WorkoutItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val workoutId: Long,
    val parentId: Long?, // null = root
    val position: Int,   // kolejność w ramach parenta
    val type: String,    // "EXERCISE" | "CIRCUIT"
    // --- Exercise ---
    val exerciseId: String?,
    val quantityValue: Int?,
    val quantityType: String?,
    // --- Circuit ---
    val phase: String?,
    val name: String?,
    val rounds: Int?,
    val structureType: String?,
    val structureData: String?, // np JSON dla EMOM/AMRAP
)