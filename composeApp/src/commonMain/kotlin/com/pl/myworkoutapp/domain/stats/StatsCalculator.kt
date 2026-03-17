package com.pl.myworkoutapp.domain.stats

import com.pl.myworkoutapp.domain.model.workout.WorkoutSession


object StatsCalculator {

    fun totalCalories(sessions: List<WorkoutSession>): Double {

        //return sessions.sumOf { it.calories ?: 0.0 }
        return 0.0
    }
}
