package com.pl.myworkoutapp.domain.model

data class WorkoutPlan(
    val id: Long,
    val name: String,
    val difficulty: Difficulty,
    val exerciseIds: List<Long>
)
