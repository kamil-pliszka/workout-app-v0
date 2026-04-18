package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.common.UiText

sealed interface ExerciseEditorEvent {
    data object Close : ExerciseEditorEvent
    data class ShowMessage(val text: UiText) : ExerciseEditorEvent
    data class ShowError(val text: UiText) : ExerciseEditorEvent
    data class Completed(val id: ExerciseId.Custom, val isNew: Boolean) : ExerciseEditorEvent
    data class Deleted(val id: ExerciseId.Custom) : ExerciseEditorEvent
}