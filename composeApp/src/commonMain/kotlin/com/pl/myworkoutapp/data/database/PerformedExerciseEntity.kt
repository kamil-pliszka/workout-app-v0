package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sessionId"),
    ]
)
data class PerformedExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val sessionId: Long,
    val builtInExerciseId: String?,
    val customExerciseId: Long?,
    val quantityType: String,
    val plannedQuantityValue: Int,
    val actualQuantityValue: Int?,
    val startTime: Instant,
    val endTime: Instant?,
    val calories: Double, //spalone kalorie
)

