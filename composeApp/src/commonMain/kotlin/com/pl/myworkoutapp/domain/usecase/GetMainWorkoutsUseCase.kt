package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.WorkoutHydrator
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.roundToInt

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
    private val hydrator: WorkoutHydrator,
    private val appSettingRepository: AppSettingRepository,
) {
    fun execute(): Flow<List<WorkoutWithMetrics>> {
        val builtInWorkouts = listOf(
            //tutaj wybieramy konkretne zestawy i określamy ich kolejność
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.SIX_PACK_20_MIN),
        )
        val builtInIds = builtInWorkouts.map { it.id }.toSet()

        return combine(
            repository.observeLatestBasedOn(builtInIds),
            repository.observeMainCustomWorkouts(),
            appSettingRepository.weightFlow
        ) { latestCustoms, standaloneCustoms, weightKg ->

            val customMap = latestCustoms.associateBy { it.basedOn }

            val mainBuiltIns = builtInWorkouts.map { builtIn ->
                customMap[builtIn.id] ?: builtIn
            }

            (mainBuiltIns + standaloneCustoms).map {
                val hydrated = hydrator.hydrate(it.id)
                WorkoutWithMetrics(
                    workout = hydrated,
                    metrics = WorkoutMetrics(
                        estimatedKcal = (hydrated.baseKcalPerKg * weightKg).roundToInt()
                    )
                )
            }
        }
    }
}