package com.pl.myworkoutapp.domain.model

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.workout.Workout

//Training Plan / Program – plan na wiele dni (np. 30-dniowy)
data class TrainingPlan(
    val id: Int,
    val name: String,
    //val category: Category,
    val difficulty: Difficulty,
    val workouts: List<Workout>,
)