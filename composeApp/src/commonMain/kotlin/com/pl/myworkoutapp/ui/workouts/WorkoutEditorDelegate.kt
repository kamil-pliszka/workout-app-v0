package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.ui.common.asUiText

sealed interface WorkoutEditorAction {
    data class OnNameChanged(val value: String) : WorkoutEditorAction
    data class OnDescChanged(val value: String) : WorkoutEditorAction
    data class OnMove(val from: Int, val to: Int) : WorkoutEditorAction
    data class ExercisePicked(val exerciseUiItem: ExerciseUiItem) : WorkoutEditorAction
    data class AddCircuit(val circuit: CircuitUiItem) : WorkoutEditorAction
    data class ModifyCircuit(val current: CircuitUiItem, val modified: CircuitUiItem) :
        WorkoutEditorAction
}

/**
 * Stateless delegate for handling workout editing logic.
 */
class WorkoutEditorDelegate {

    private fun WorkoutWithExercisesUiModel.updateWorkout(
        block: (WorkoutUiModel) -> WorkoutUiModel
    ): WorkoutWithExercisesUiModel {
        return copy(workout = block(this.workout))
    }

    private fun WorkoutWithExercisesUiModel.updateItems(
        block: (MutableList<WorkoutUiItem>) -> Unit
    ): WorkoutWithExercisesUiModel {
        val mutableItems = items.toMutableList()
        block(mutableItems)
        return copy(items = mutableItems)
    }

    fun reduce(
        state: WorkoutWithExercisesUiModel,
        action: WorkoutEditorAction
    ): WorkoutWithExercisesUiModel {
        return when (action) {
            is WorkoutEditorAction.OnMove -> {
                state.updateItems { moveWorkoutItem(it, action.from, action.to) }
            }

            is WorkoutEditorAction.OnDescChanged -> {
                state.updateWorkout { it.copy(desc = action.value.asUiText()) }
            }

            is WorkoutEditorAction.OnNameChanged -> {
                state.updateWorkout { it.copy(name = action.value.asUiText()) }
            }

            is WorkoutEditorAction.ExercisePicked -> {
                state.updateItems { addExercise(it, action.exerciseUiItem) }
            }

            is WorkoutEditorAction.AddCircuit -> {
                state.updateItems { addCircuit(it, action.circuit) }
            }

            is WorkoutEditorAction.ModifyCircuit -> {
                state.updateItems { modifyCircuit(it, action.current, action.modified) }
            }
        }
    }

    /*
    private fun <T> List<T>.move(from: Int, to: Int): List<T> {
        if (from == to || from !in indices || to !in indices) return this
        val mutable = toMutableList()
        val item = mutable.removeAt(from)
        mutable.add(to, item)
        return mutable
    }
    */

    private fun moveWorkoutItem(items: MutableList<WorkoutUiItem>, from: Int, to: Int) {
        //TODO - tutaj trzeba sie zastanowic co z timeline + circuit elementami
        val item = items.removeAt(from)
        items.add(to, item)
        /* docelowo coś w stylu:
            val item = this[from]
            when (item) {
                is ExerciseUiItem -> moveExercise(from, to)
                is CircuitUiItem -> moveCircuit(from, to)
            }
            normalizeTimeline()
         */
    }

    private fun addExercise(items: MutableList<WorkoutUiItem>, exercise: ExerciseUiItem) {
        println("addExercise : ${exercise.exerciseId}")
        val maxKey = items.maxOf { it.key }
        items.add(
            exercise.copy(
                key = maxKey + 1,
                timeline = listOf(TimeLineItemType.End(Color.Red))
            )
        )
    }

    private fun addCircuit(items: MutableList<WorkoutUiItem>, circuit: CircuitUiItem) {
        println("addCircuit : $circuit")
        val maxKey = items.maxOf { it.key }
        items.add(
            circuit.copy(
                key = maxKey + 1,
                timeline = listOf(TimeLineItemType.End(Color.Red))
            )
        )
    }


    private fun modifyCircuit(
        items: MutableList<WorkoutUiItem>,
        current: CircuitUiItem, modified: CircuitUiItem
    ) {
        val index = items.indexOf(current)
        require(index >= 0)
        items[index] = current.copy(
            phase = modified.phase,
            rounds = modified.rounds,
            structure = modified.structure,
            title = modified.title,
        )
    }

}