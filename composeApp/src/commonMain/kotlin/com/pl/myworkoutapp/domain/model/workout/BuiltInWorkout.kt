package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.Difficulty

data class BuiltInWorkout(
    override val id: WorkoutId.BuiltIn,
    override val difficulty: Difficulty,
    //override val estimatedDuration: Int, //in seconds
    override val items: List<WorkoutItem>,
    override val estimatedDuration: Int? = null,//in seconds
    override val baseKcalPerKg: Double? = null,
    override val estimatedKcal: Int? = null,
) : Workout