package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.BuiltInWorkoutCatalog
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Zwraca listę workoutów na głównej liście workouts,
 * to będzie specjaloziwany UC dla zasilenia jednej formatki
 * Idea - mamy zestaw workouts(builtin) jako baza
 * Na to nakładamy ostatni custom workout który ma baseOn = dany circuit
 * Do wyniku dokładane są workout które mają baseOn = null - czyli zrobione od zera przez użytkownika
 *
 * Built-in - metryki są derived at read time
 * Custom - metryki są materialized at write time
 */
class GetMainWorkoutsUseCase(
    private val repository: WorkoutRepository,
    private val catalog: BuiltInWorkoutCatalog
) {
    fun execute(
        weightKg : Double = 100.0, //TODO - na razie zahardkodowana wartość
    ): Flow<List<Workout>> {
        val builtInWorkouts = listOf(
            //tutaj wybieramy konkretne zestawy i określamy ich kolejność
            catalog.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET),
            catalog.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET),
            catalog.get(BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET),
            catalog.get(BuiltInWorkoutId.SIX_PACK_20_MIN),
        )
        val builtInIds = builtInWorkouts.map { it.id }.toSet()

        return combine(
            repository.observeLatestBasedOn(builtInIds),
            repository.observeMainCustomWorkouts()
        ) { latestCustoms, standaloneCustoms ->

            val customMap = latestCustoms.associateBy { it.basedOn }

            val mainBuiltIns = builtInWorkouts.map { builtIn ->
                customMap[builtIn.id] ?: builtIn
            }

            (mainBuiltIns + standaloneCustoms).map {
                it.withEstimatedKcalForWeight(weightKg)
            }
        }
    }
}