package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.exercises.mapQuantityValue
import com.pl.myworkoutapp.ui.exercises.quantityChange

sealed interface WorkoutEditorAction {
    data class OnNameChanged(val value: String) : WorkoutEditorAction
    data class OnDescChanged(val value: String) : WorkoutEditorAction
    data class OnMove(val from: Int, val to: Int) : WorkoutEditorAction
    data class ExercisePicked(val exerciseUiItem: ExerciseUiItem) : WorkoutEditorAction
    object OnCancelCircuitEditor : WorkoutEditorAction
    object OnAddCircuit : WorkoutEditorAction
    data class OnEditCircuit(val circuit: CircuitUiItem) : WorkoutEditorAction
    data class OnDeleteCircuit(val circuit: CircuitUiItem) : WorkoutEditorAction
    data class OnDeleteExercise(val exercise: ExerciseUiItem) : WorkoutEditorAction
    object OnSaveCircuitEditor : WorkoutEditorAction
    object OnDeleteElementConfirm: WorkoutEditorAction
    object OnDeleteElementCancel: WorkoutEditorAction
    data class OnChangeQuantity(val exercise: ExerciseUiItem, val increase: Boolean) : WorkoutEditorAction
    data class OnExerciseExchangeStart(val exercise: ExerciseUiItem) : WorkoutEditorAction
}

/**
 * Stateless delegate for handling workout editing logic.
 */
class WorkoutEditorDelegate(
    private val circuitDelegate: CircuitEditorDelegate,
) {

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
                if (state.exchangingExerciseItem != null) {
                    val newState = state.updateItems { exchangeExercise(it, state.exchangingExerciseItem, action.exerciseUiItem) }
                    newState.copy( exchangingExerciseItem = null)
                } else {
                    state.updateItems { addExercise(it, action.exerciseUiItem) }
                }
            }

            /*is WorkoutEditorAction.AddCircuit -> {
                val newState = state.updateItems { addCircuit(it, action.circuit) }
                newState.copy(scrollToIdx = newState.items.lastIndex)
            }

            is WorkoutEditorAction.ModifyCircuit -> {
                state.updateItems { modifyCircuit(it, action.current, action.modified) }
            }*/

            WorkoutEditorAction.OnCancelCircuitEditor -> {
                state.copy(
                    editableCircuit = null,
                    editingCircuitItem = null
                )
            }

            WorkoutEditorAction.OnAddCircuit -> {
                val editableCircuit = CircuitEditorUiState(isNew = true)
                state.copy(
                    editableCircuit = editableCircuit.copy(
                        isValid = circuitDelegate.validate(
                            editableCircuit
                        )
                    ),
                    editingCircuitItem = null,
                )
            }

            is WorkoutEditorAction.OnEditCircuit -> {
                val editableCircuit = action.circuit.toCircuitEditorUiState()
                state.copy(
                    editableCircuit = editableCircuit.copy(
                        isValid = circuitDelegate.validate(
                            editableCircuit
                        )
                    ),
                    editingCircuitItem = action.circuit,
                )
            }

            WorkoutEditorAction.OnSaveCircuitEditor -> {
                val current = state.editableCircuit ?: return state
                if (state.editingCircuitItem == null) {
                    //NEW CIRCUIT
                    val newState = state.updateItems { addCircuit(it, current) }
                    newState.copy(
                        scrollToIdx = newState.items.lastIndex,
                        editableCircuit = null,
                        editingCircuitItem = null
                    )
                } else {
                    val newState =
                        state.updateItems { modifyCircuit(it, state.editingCircuitItem, current) }
                    newState.copy(
                        editableCircuit = null,
                        editingCircuitItem = null
                    )
                }
            }

            is WorkoutEditorAction.OnDeleteCircuit -> {
                state.copy( deletingWorkoutItem = action.circuit)
            }
            is WorkoutEditorAction.OnDeleteExercise -> {
                state.copy( deletingWorkoutItem = action.exercise)
            }

            WorkoutEditorAction.OnDeleteElementCancel -> {
                state.copy( deletingWorkoutItem = null)
            }
            WorkoutEditorAction.OnDeleteElementConfirm -> {
                val toDelete = state.deletingWorkoutItem ?: return state
                val newState = state.updateItems { deleteWorkoutItem(it, toDelete) }
                newState.copy( deletingWorkoutItem = null )
            }

            is WorkoutEditorAction.OnChangeQuantity -> {
                state.updateItems { changeQuantity(it, action.exercise, action.increase) }
            }

            is WorkoutEditorAction.OnExerciseExchangeStart -> {
                state.copy( exchangingExerciseItem = action.exercise)
            }
        }
    }

    fun reduce(
        state: WorkoutWithExercisesUiModel,
        action: CircuitEditorAction
    ): WorkoutWithExercisesUiModel {
        if (state.editableCircuit == null) return state
        return state.copy(
            editableCircuit = circuitDelegate.reduce(state.editableCircuit, action)
        )
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

    private fun addCircuit(items: MutableList<WorkoutUiItem>, circuit: CircuitEditorUiState) {
        println("addCircuit : $circuit")
        val maxKey = items.maxOf { it.key }
        items.add(
            CircuitUiItem(
                phase = circuit.phase,
                structure = circuit.toStructure(),
                title = circuit.name,
                key = maxKey + 1,
                timeline = listOf(TimeLineItemType.End(Color.Red))
            )
        )
    }


    private fun modifyCircuit(
        items: MutableList<WorkoutUiItem>,
        current: CircuitUiItem, modified: CircuitEditorUiState
    ) {
        val index = items.indexOf(current)
        require(index >= 0)
        items[index] = current.copy(
            phase = modified.phase,
            structure = modified.toStructure(),
            title = modified.name,
        )
    }

    private fun deleteWorkoutItem(items: MutableList<WorkoutUiItem>, toDelete: WorkoutUiItem) {
        items.remove(toDelete)
    }

    private fun changeQuantity(items: MutableList<WorkoutUiItem>,
        exercise: ExerciseUiItem, increase: Boolean
    ) {
        val index = items.indexOf(exercise)
        require(index >= 0)
        val newQuantityValue = quantityChange(
            type = exercise.quantityType,
            currentQuantityValua = exercise.quantityValue,
            increase = increase
        )
        items[index] = exercise.copy(
            quantityValue = newQuantityValue,
        )
    }


    private fun exchangeExercise(items: MutableList<WorkoutUiItem>,
        existingExercise: ExerciseUiItem, newExercise: ExerciseUiItem
    ) {
        val index = items.indexOf(existingExercise)
        require(index >= 0)

        val newQuantityValue = mapQuantityValue(
            existingExercise.quantityType,
            existingExercise.quantityValue,
            newExercise.quantityType
        )
        items[index] = newExercise.copy(
            quantityValue = newQuantityValue,
            key = existingExercise.key,
            timeline = existingExercise.timeline,
        )
    }

}