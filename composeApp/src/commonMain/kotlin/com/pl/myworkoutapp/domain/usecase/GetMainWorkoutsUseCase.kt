package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Zwraca listę workoutów na głównej liście workouts,
 * to będzie specjaloziwany UC dla zasilenia jednej formatki
 * Idea - mamy zestaw workouts(builtin) jako baza
 * Na to nakładamy ostatni custom workout który ma baseOn = dany circuit
 * Docelowo - odfiltrować workouty które będą częścią WorkoutPlan - ale na razie model nie przewiduje takiej informacji
 */
class GetMainWorkoutsUseCase(
    private val repository: WorkoutRepository
) {
    fun execute(): Flow<List<Workout>> {
        val builtInWorkouts = listOf(
            //tutaj wybieramy konkretne zestawy i określamy ich kolejność
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET),
            BuiltInWorkoutRegistry.get(BuiltInWorkoutId.SIX_PACK_20_MIN),
        )
        return repository
            .observeLatestBasedOn(
                builtInWorkouts.map { it.id }.toSet()
            ).map { customWorkouts ->
                val customMap = customWorkouts.associateBy { it.basedOn }

                builtInWorkouts.map { builtIn ->
                    customMap[builtIn.id] ?: builtIn
                }
            }
    }
}