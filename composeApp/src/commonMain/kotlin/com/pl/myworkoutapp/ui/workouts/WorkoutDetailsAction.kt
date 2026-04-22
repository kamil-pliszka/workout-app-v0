package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoAction
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesAction

sealed interface WorkoutDetailsAction {
    object OnStartWorkout : WorkoutDetailsAction
    object OnSaveWorkout : WorkoutDetailsAction
    object OnResetWorkout : WorkoutDetailsAction
    data class ShowExerciseInfo(val exercise: WorkoutUiItem) : WorkoutDetailsAction
    object CloseExerciseInfo : WorkoutDetailsAction
    object ShowExercisePicker : WorkoutDetailsAction
    object ExerciseSave : WorkoutDetailsAction
    object ExerciseReset : WorkoutDetailsAction
    object ExercisePrev : WorkoutDetailsAction
    object ExerciseNext : WorkoutDetailsAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutDetailsAction
    data class ExercisePicked(val exerciseId: ExerciseId?) : WorkoutDetailsAction
    object OnBack : WorkoutDetailsAction
    object OnOpenEditor : WorkoutDetailsAction
    object OnCloseEditor : WorkoutDetailsAction
    object OnSaveEditor : WorkoutDetailsAction
    object OnResetEditor : WorkoutDetailsAction
    object OnDeleteRequest : WorkoutDetailsAction
    object OnTuneRequest : WorkoutDetailsAction
}

fun WorkoutExerciseInfoAction.toWorkoutDetailsAction(): WorkoutDetailsAction = when (this) {
    is WorkoutExerciseInfoAction.ChangeQuantity -> WorkoutDetailsAction.ChangeQuantity(increase)
    WorkoutExerciseInfoAction.CloseExerciseInfo -> WorkoutDetailsAction.CloseExerciseInfo
    WorkoutExerciseInfoAction.ExerciseNext -> WorkoutDetailsAction.ExerciseNext
    WorkoutExerciseInfoAction.ExercisePrev -> WorkoutDetailsAction.ExercisePrev
    WorkoutExerciseInfoAction.ExerciseReset -> WorkoutDetailsAction.ExerciseReset
    WorkoutExerciseInfoAction.ExerciseSave -> WorkoutDetailsAction.ExerciseSave
    WorkoutExerciseInfoAction.ShowExercisePicker -> WorkoutDetailsAction.ShowExercisePicker
}

fun WorkoutWithExercisesAction.toWorkoutDetailsAction(): WorkoutDetailsAction = when (this) {
    WorkoutWithExercisesAction.OnBack -> WorkoutDetailsAction.OnBack
    WorkoutWithExercisesAction.OnDeleteRequest -> WorkoutDetailsAction.OnDeleteRequest
    WorkoutWithExercisesAction.OnOpenEditor -> WorkoutDetailsAction.OnOpenEditor
    WorkoutWithExercisesAction.OnTuneRequest -> WorkoutDetailsAction.OnTuneRequest
    is WorkoutWithExercisesAction.ShowExerciseInfo -> WorkoutDetailsAction.ShowExerciseInfo(exercise)
}
