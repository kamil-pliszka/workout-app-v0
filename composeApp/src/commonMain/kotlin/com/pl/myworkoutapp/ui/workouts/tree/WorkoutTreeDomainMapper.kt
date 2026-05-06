package com.pl.myworkoutapp.ui.workouts.tree

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.workouts.*


/**
 * Dokouje przekształcenia, ale tylko dla ćwiczeń typu builtin (te nie wymagają dostępu do repo)
 */
fun transform(workout: Workout, metrics: WorkoutMetrics): WorkoutWithExercisesUiModel {
    val exerciseIds = workout.items.extractExerciseIds()
    val exercises = exerciseIds.map { id ->
        when (id) {
            is ExerciseId.BuiltIn -> BuiltInExerciseRegistry.get((id).id)
            is ExerciseId.Custom -> error("Custom not supported")
        }
    }.toSet()
    return transform(workout, metrics, exercises)
}

fun List<WorkoutItem>.toTree(
    exercises: Map<ExerciseId, Exercise>,
    parent: CircuitNode? = null
): List<TreeNode> {
    return map { item ->
        when (item) {
            is WorkoutExercise -> {
                ExerciseNode(
                    parent = parent,
                    exercise = item.toUiBase(exercises.getValue(item.exerciseId))
                )
            }

            is Circuit -> {
                val node = CircuitNode(
                    parent = parent,
                    circuit = item.toUiBase()
                )
                node.children += item.items.toTree(exercises, node)
                node
            }
        }
    }
}

fun transform(workout: Workout, metrics: WorkoutMetrics, exercises: Set<Exercise>): WorkoutWithExercisesUiModel {
    val exercisesMap = exercises.associateBy { it.id }
    val workoutUiModel = workout.toUi(metrics)

    val rootNodes = workout.items.toTree(exercisesMap)
    val itemsUi = rootNodes.normalizeToUi().mapIndexed { idx, item ->
        when (item) {
            is CircuitUiItem -> item.copy(key = idx)
            is ExerciseUiItem -> item.copy(key = idx)
        }
    }

    return WorkoutWithExercisesUiModel(
        workout = workoutUiModel,
        items = itemsUi
    )
}

fun List<TreeNode>.toDomain(): List<WorkoutItem> =
    map { it.toDomain() }

fun TreeNode.toDomain(): WorkoutItem =
    when (this) {
        is ExerciseNode -> WorkoutExercise(
            exerciseId = exercise.exerciseId,
            quantity = Quantity(exercise.quantityType, exercise.quantityValue)
        )

        is CircuitNode -> Circuit(
            phase = circuit.phase,
            name = null,
            structure = circuit.structure.toDomain(),
            items = children.toDomain()
        )
    }
