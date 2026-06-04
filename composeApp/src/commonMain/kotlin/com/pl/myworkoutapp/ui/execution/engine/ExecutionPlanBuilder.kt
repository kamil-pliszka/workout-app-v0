package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem

/**
 * Transformer:
 * Workout -> List<ExecutionStep>
 *
 * Odpowiada za:
 *
 * flatten drzewa workoutu
 * generowanie stepów runtime
 * dodawanie restów
 * później:
 * circuits
 * supersets
 * warmup
 * auto rest rules
 *
 * To jest adapter między domeną a runtime engine.
 */
class ExecutionPlanBuilder {

    fun build(
        workout: Workout,
        exercises: Set<Exercise>, //resolved exercises
    ): ExecutionPlan {
        val result = mutableListOf<ExecutionStep>()
        val exercisesMap = exercises.associateBy { it.id }
        appendItems(
            items = workout.items,
            exercisesMap = exercisesMap,
            result = result
        )
        return ExecutionPlan(result)
    }

    private fun appendItems(
        items: List<WorkoutItem>,
        exercisesMap: Map<ExerciseId, Exercise>,
        result: MutableList<ExecutionStep>,
    ) {
        items.forEachIndexed { index, item ->
            when (item) {
                is WorkoutExercise -> {
                    val exercise = exercisesMap.getValue(item.exerciseId)

                    result += ExecutionStep.ExerciseStep(
                        exercise = exercise,
                        quantity = item.quantity,
                    )
                    val isLast = index == items.lastIndex
                    if (!isLast) {
                        result += ExecutionStep.RestStep(
                            durationSeconds = 30 //TODO - docelowo z ustawien usera
                        )
                    }
                }
                is Circuit -> {
                    appendItems(
                        items = item.items,
                        exercisesMap = exercisesMap,
                        result = result
                    )
                }
            }
        }
    }
}