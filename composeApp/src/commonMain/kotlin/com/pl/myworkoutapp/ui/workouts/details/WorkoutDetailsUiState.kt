package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel
import com.pl.myworkoutapp.ui.workouts.WorkoutWithExercisesUiModel


data class WorkoutDetailsUiState(
    val mode: WorkoutDetailsMode
)

sealed interface WorkoutDetailsMode {
    object Loading : WorkoutDetailsMode
    data class View(
        val session: WorkoutViewSession
    ) : WorkoutDetailsMode

    data class Edit(
        val session: WorkoutEditSession
    ) : WorkoutDetailsMode
}

sealed interface WorkoutViewModal {
    data object ExercisePicker : WorkoutViewModal
    data object ConfirmDelete : WorkoutViewModal
    data object ConfirmReset : WorkoutViewModal
}

sealed interface WorkoutEditModal {
    data class ExercisePicker(
        val context: ExercisePickerContext,
        val currentExerciseId: ExerciseId?
    ) : WorkoutEditModal

    data object ConfirmDiscardChanges : WorkoutEditModal
    data class ConfirmDeleteItem(val key: Int) : WorkoutEditModal
}

sealed interface ExercisePickerContext {
    data object AddExercise : ExercisePickerContext
    data class ReplaceListItem(val key: Int) : ExercisePickerContext
    data object ReplacePreview : ExercisePickerContext
}

data class ActiveExerciseSession(
    val key: Int,
    val original: ExerciseInfoUiModel,
    val draft: ExerciseInfoUiModel,
)

data class WorkoutViewSession(
    override val workout: WorkoutWithExercisesUiModel,
    override val activeExercise: ActiveExerciseSession? = null,
    val hasUnsavedChanges: Boolean = false,
    val modal: WorkoutViewModal? = null,
) : ExerciseInteractionHost<WorkoutViewSession> {

    override fun withWorkout(workout: WorkoutWithExercisesUiModel): WorkoutViewSession =
        copy(workout = workout)

    override fun withActiveExercise(
        activeExercise: ActiveExerciseSession?
    ): WorkoutViewSession = copy(activeExercise = activeExercise)
}

data class WorkoutEditSession(
    val original: WorkoutWithExercisesUiModel,
    override val workout: WorkoutWithExercisesUiModel,//draft

    override val activeExercise: ActiveExerciseSession? = null,
    val modal: WorkoutEditModal? = null,

    val editableCircuit: CircuitEditorUiState? = null,
    val editingCircuitItemKey: Int? = null,
    val editableMetadata: WorkoutMetadataDraft? = null,
) : ExerciseInteractionHost<WorkoutEditSession> {

    override fun withWorkout(workout: WorkoutWithExercisesUiModel): WorkoutEditSession =
        copy(workout = workout)

    override fun withActiveExercise(
        activeExercise: ActiveExerciseSession?
    ): WorkoutEditSession = copy(activeExercise = activeExercise)

}
