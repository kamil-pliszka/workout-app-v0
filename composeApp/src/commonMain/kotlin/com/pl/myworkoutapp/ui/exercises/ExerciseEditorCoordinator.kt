package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface ExerciseResult {
    data class Created(val exerciseId: ExerciseId.Custom) : ExerciseResult
    data class Updated(val exerciseId: ExerciseId.Custom) : ExerciseResult
    data class Deleted(val exerciseId: ExerciseId.Custom) : ExerciseResult
}

/**
 * Koordynator wyjścia z dialogu ExerciseEditorScreen
 * Gdy zakończy się działanie tej funkcjonalności,
 * zostanie wyemitowane odpowiednie zdarzenie dostępne w events
 */
class ExerciseEditorCoordinator {
    private val _events = MutableSharedFlow<ExerciseResult>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    private fun emit(result: ExerciseResult) {
        _events.tryEmit(result)
        println("ExerciseEditorCoordinator emited : $result")
    }

    fun exerciseCreated(exerciseId: ExerciseId.Custom) {
        emit(ExerciseResult.Created(exerciseId))
    }

    fun exerciseUpdated(exerciseId: ExerciseId.Custom) {
        emit(ExerciseResult.Updated(exerciseId))
    }

    fun exerciseDeleted(exerciseId: ExerciseId.Custom) {
        emit(ExerciseResult.Deleted(exerciseId))
    }

}