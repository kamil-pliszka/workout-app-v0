package com.pl.myworkoutapp.domain.calories

object CaloriesCalculator {

    fun calculate(met: Double, weightKg: Double, durationSeconds: Int): Double {

        val hours = durationSeconds / 3600.0

        return met * weightKg * hours
    }
}
