package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.common.DropPosition

sealed interface WorkoutTreeMutation {

    data class Move(
        val draggedKey: Int,
        val targetKey: Int,
        val position: DropPosition
    ) : WorkoutTreeMutation

    data class InsertExercise(
        val exercise: ExerciseUiItem,
        val targetKey: Int?,
        val position: DropPosition
    ) : WorkoutTreeMutation

    data class InsertCircuit(
        val circuit: CircuitUiItem,
        val targetKey: Int?,
        val position: DropPosition
    ) : WorkoutTreeMutation

    data class Delete(
        val key: Int
    ) : WorkoutTreeMutation

    data class ReplaceExercise(
        val key: Int,
        val newExercise: ExerciseUiItem
    ) : WorkoutTreeMutation
}