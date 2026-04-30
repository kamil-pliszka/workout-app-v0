package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId

sealed interface WorkoutsEvent {
    //    data object Close : WorkoutsEvent
//    data class ShowSuccess(val text: UiText) : WorkoutsEvent
//    data class ShowError(val text: UiText) : WorkoutsEvent
    data class NavToWorkoutExecution(val workoutId: WorkoutId) : WorkoutsEvent
    data class NavToWorkoutDetails(val workoutId: WorkoutId) : WorkoutsEvent
    data class NavToExerciseEditor(val exerciseId: ExerciseId) : WorkoutsEvent
    //data class NavToWorkoutEditor(val workoutId: WorkoutId): WorkoutsEvent
}