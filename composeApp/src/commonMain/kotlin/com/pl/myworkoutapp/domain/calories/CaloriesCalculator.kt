package com.pl.myworkoutapp.domain.calories

object CaloriesCalculator {

    //TODO - do kompletnego napisania
    //Problem - nie uwzględnia intensywności
    //nie uwzględnia przerw (a masz je w Circuit!)
    //przyszłość: fun calculate(workoutSession: WorkoutSession, user: UserProfile)
    //albo fun calculate(workout, , user: UserProfile)
    fun calculate(met: Double, weightKg: Double, durationSeconds: Int): Double {

        val hours = durationSeconds / 3600.0

        return met * weightKg * hours
    }
}
