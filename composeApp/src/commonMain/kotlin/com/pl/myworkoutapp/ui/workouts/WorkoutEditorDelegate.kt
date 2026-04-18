package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.common.asUiText

sealed interface WorkoutEditorAction {
    data class OnNameChanged(val value: String) : WorkoutEditorAction
    data class OnDescChanged(val value: String) : WorkoutEditorAction
    data class OnMove(val from: Int, val to: Int) : WorkoutEditorAction
}
//🔥 To NIE jest ViewModel, to NIE jest “ciężki serwis”
//To jest:
//👉 stateless service / use-case / reducer provider
class WorkoutEditorDelegate {

    private fun WorkoutWithExercisesUiModel.updateWorkout(
        block: (WorkoutUiModel) -> WorkoutUiModel
    ) : WorkoutWithExercisesUiModel {
        return copy(workout = block(this.workout))
    }

    fun reduce(
        state: WorkoutWithExercisesUiModel,
        action: WorkoutEditorAction
    ): WorkoutWithExercisesUiModel {
        return when (action) {
            is WorkoutEditorAction.OnMove -> {
                val items = state.items.toMutableList().apply {
                    moveWorkoutItem(action.from, action.to)
                }
                state.copy(items = items)
            }
            is WorkoutEditorAction.OnDescChanged -> state.copy(workout = state.workout.copy(
                desc = action.value.asUiText()
            ))
            is WorkoutEditorAction.OnNameChanged -> state.copy(workout = state.workout.copy(
                name = action.value.asUiText()
            ))
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

    private fun MutableList<WorkoutUiItem>.moveWorkoutItem(from: Int, to: Int) {
        //TODO - tutaj trzeba sie zastanowic co z timeline + circuit elementami
        val item = removeAt(from)
        add(to, item)
        /* docelowo coś w stylu:
            val item = this[from]
            when (item) {
                is ExerciseUiItem -> moveExercise(from, to)
                is CircuitUiItem -> moveCircuit(from, to)
            }
            normalizeTimeline()
         */
    }

}