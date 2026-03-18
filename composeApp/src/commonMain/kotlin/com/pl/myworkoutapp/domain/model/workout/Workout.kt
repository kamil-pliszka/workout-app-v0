package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.Difficulty

//Workout – zestaw ćwiczeń wykonywanych w jednej sesji
sealed interface Workout {
    val id: WorkoutId
    val difficulty: Difficulty
    //val estimatedDuration: Int //in seconds
    val exercises: List<WorkoutItem>
}