package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.Difficulty

data class BuiltInWorkout(
    override val id: WorkoutId.BuiltIn,
    override val difficulty: Difficulty,
    override val estimatedDuration: Int, //in seconds
    override val exercises: List<WorkoutItem>,
) : Workout