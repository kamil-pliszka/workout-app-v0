package com.pl.myworkoutapp.domain.stats

import com.pl.myworkoutapp.domain.model.WorkoutSession


object StatsCalculator {

    fun totalCalories(sessions: List<WorkoutSession>): Double {

        return sessions.sumOf { it.calories }
    }
}
