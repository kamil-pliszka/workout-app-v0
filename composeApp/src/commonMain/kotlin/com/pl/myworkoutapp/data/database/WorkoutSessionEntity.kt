package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant


@Entity(
    indices = [
        Index("planId"),
        Index("workoutId"),
    ]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val planId: String?,
    val workoutId: String,
    val startTime: Instant,
    val endTime: Instant?,
    val progress: Float,
    val completed: Boolean = false,
    val estimatedDuration: Int,//in seconds
    val estimatedCalories: Int,//in kcal
    val executionTime: Double, //czas samych ćwiczeń(bez przerw), aktualizowane po każdym ćwiczeniu
    val calories: Double, //spalone kalorie, aktualizowane po każdym ćwiczeniu
    val currentStepIndex: Int?, //aktualny krok treningu, 0 - oznacza pierwszy krok
    val updatedAt: Long = 0,
)

