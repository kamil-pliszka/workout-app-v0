package com.pl.myworkoutapp.domain.model

data class WorkoutSession(
    val id: Long,
    val planId: Long,
    val startTime: Long,
    val endTime: Long,
    val calories: Double
)
