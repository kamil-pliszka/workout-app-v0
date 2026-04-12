package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant


@Entity
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val planId: Long,
    val startTime: Instant?,
    val endTime: Instant?,
    val calories: Double?,
)
