package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.user.Settings

/**
 * Oblicza szacowany czas treningu (w sekundach) bez przerw, czyli czas netto
 * jak też czas z uwzględnieniem przerw, czyli czas brutto
 * //TODO - do zrobienia kiedyś, na razie zaślepka
 */
object WorkoutEstimator {

    fun estimate(workout: Workout, settings: Settings): Pair<Int, Int> {
        //TODO
        return Pair(123, 456)
    }

}