package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType

sealed interface ExerciseEditorAction {
    object OnDismissRequest: ExerciseEditorAction
    object OnDeleteAction: ExerciseEditorAction
    object OnSaveAction: ExerciseEditorAction
    object OnScreenEntered: ExerciseEditorAction
    object OnScreenExited: ExerciseEditorAction
    //data class OnPageChanged(val index: Int) : ExerciseEditorAction
    //data class NavToWorkout(val workoutId: WorkoutId) : ExerciseEditorAction
    //data class NavToExercise(val exerciseId: ExerciseId) : ExerciseEditorAction
    data class NameChanged(val value: String): ExerciseEditorAction
    data class DescriptionChanged(val value: String): ExerciseEditorAction
    //object PickImage: ExerciseEditorAction
    data class OnImagePicked(val path: String?) : ExerciseEditorAction
    object RemoveImage: ExerciseEditorAction
    data class MuscleChanged(val value: MuscleGroup): ExerciseEditorAction
    data class ExerciseTypeChanged(val value: ExerciseType): ExerciseEditorAction
    data class EquipmentChanged(val value: Equipment): ExerciseEditorAction
    data class QuantityTypeChanged(val value: QuantityType): ExerciseEditorAction
    data class MetChanged(val value: String): ExerciseEditorAction

}