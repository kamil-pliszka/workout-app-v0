package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.Difficulty

data class BuiltInWorkout(
    override val id: WorkoutId.BuiltIn,
    override val difficulty: Difficulty,
    override val items: List<WorkoutItem>,
    override val estimatedDuration: Int = 0,//in seconds
    override val baseKcalPerKg: Double = 0.0,
) : Workout