package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.domain.model.workout.extractExerciseIds
import com.pl.myworkoutapp.domain.model.workout.toBuiltInWorkoutId
import com.pl.myworkoutapp.domain.usecase.ResolveWorkoutMetricsUseCase
import kotlin.collections.mapValues

/**
 * precomputed view model source dla built-in content
 */
class BuiltInWorkoutCatalog(
    private val resolveWorkoutMetricsUseCase: ResolveWorkoutMetricsUseCase
) {

    private val cache: Map<BuiltInWorkoutId, BuiltInWorkout> =
        buildCache()

    fun get(id: BuiltInWorkoutId): BuiltInWorkout =
        cache[id] ?: error("Missing built-in workout: $id")

    fun getAll(): List<BuiltInWorkout> =
        cache.values.toList()

    private fun buildCache(): Map<BuiltInWorkoutId, BuiltInWorkout> {
        return BuiltInWorkoutRegistry.getAll()
            .associateBy { it.id.toBuiltInWorkoutId() }
            .mapValues { (_, workout) -> enrich(workout) }
    }

    private fun enrich(workout: BuiltInWorkout): BuiltInWorkout {
        val exercises = resolveExercises(workout)
        return resolveWorkoutMetricsUseCase.execute(workout, exercises)
    }

    private fun resolveExercises(workout: BuiltInWorkout): Set<Exercise> {
        val exerciseIds = workout.items.extractExerciseIds()

        val resolved = exerciseIds.map { id ->
            when (id) {
                is ExerciseId.BuiltIn ->
                    BuiltInExerciseRegistry.get(id.id)

                is ExerciseId.Custom ->
                    error("Custom exercises not supported in BuiltInWorkoutCatalog")
            }
        }

        return resolved.toSet()
    }
}