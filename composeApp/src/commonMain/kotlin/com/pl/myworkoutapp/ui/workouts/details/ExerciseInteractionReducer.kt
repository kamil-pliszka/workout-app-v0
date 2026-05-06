package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.workouts.ExerciseUiItem
import com.pl.myworkoutapp.ui.workouts.WorkoutWithExercisesUiModel

/**
 * To jest shared core dla flow aktywnego ćwiczenia.
 *
 * Obsługuje wyłącznie:
 *
 * open exercise
 * close exercise
 * next / prev
 * change quantity
 * reset quantity
 * exchange exercise
 * save exercise
 *
 * Nie interesuje go:
 *
 * save whole workout
 * start workout
 * circuit editor
 * delete item
 * modale ekranu
 *
 * To jest bardzo ważne: ten reducer nie zna kontekstu ekranu.
 * On zna tylko:
 *
 * „mam workout + active exercise flow”
 */

sealed interface ExerciseInteractionAction {
    data class Open(val key: Int, val info: ExerciseInfoUiModel) : ExerciseInteractionAction
    data object Close : ExerciseInteractionAction
    data object Next : ExerciseInteractionAction
    data object Prev : ExerciseInteractionAction
    data class ChangeQuantity(val increase: Boolean) : ExerciseInteractionAction
    data object Reset : ExerciseInteractionAction
    data class Exchange(val newInfo: ExerciseInfoUiModel) : ExerciseInteractionAction
    data object Save : ExerciseInteractionAction
}

interface ExerciseInteractionHost<T : ExerciseInteractionHost<T>> {
    val workout: WorkoutWithExercisesUiModel
    val activeExercise: ActiveExerciseSession?

    fun withWorkout(workout: WorkoutWithExercisesUiModel): T
    fun withActiveExercise(activeExercise: ActiveExerciseSession?): T
}

data class ExerciseInteractionResult<T : ExerciseInteractionHost<T>>(
    val state: T,
    val effect: ExerciseInteractionEffect? = null,
)

sealed interface ExerciseInteractionEffect {
    data class LoadExerciseInfo(val key: Int, val exerciseId: ExerciseId) :
        ExerciseInteractionEffect
}

class ExerciseInteractionReducer {

    fun <T : ExerciseInteractionHost<T>> reduce(
        state: T,
        action: ExerciseInteractionAction
    ): ExerciseInteractionResult<T> {
        return when (action) {
            is ExerciseInteractionAction.Open ->
                open(state, action.key, action.info)

            ExerciseInteractionAction.Close ->
                ExerciseInteractionResult(close(state))

            ExerciseInteractionAction.Next ->
                navigate(state, +1)

            ExerciseInteractionAction.Prev ->
                navigate(state, -1)

            is ExerciseInteractionAction.ChangeQuantity ->
                ExerciseInteractionResult(changeQuantity(state, action.increase))

            ExerciseInteractionAction.Reset ->
                ExerciseInteractionResult(reset(state))

            is ExerciseInteractionAction.Exchange ->
                ExerciseInteractionResult(exchange(state, action.newInfo))

            ExerciseInteractionAction.Save ->
                ExerciseInteractionResult(save(state))
        }
    }

    private fun <T : ExerciseInteractionHost<T>> open(
        host: T,
        key: Int,
        info: ExerciseInfoUiModel
    ): ExerciseInteractionResult<T> {
        val exercises = host.workout.items.filterIsInstance<ExerciseUiItem>()
        val position = exercises.indexOfFirst { it.key == key } + 1
        val current = exercises.first { it.key == key }

        val infoCompleted = info.copy(
            quantityValue = current.quantityValue,
            position = position,
            total = exercises.size,
        )
        val active = ActiveExerciseSession(
            key = key,
            original = infoCompleted,
            draft = infoCompleted,
        )

        return ExerciseInteractionResult(
            state = host.withActiveExercise(active)
        )
    }

    private fun <T : ExerciseInteractionHost<T>> close(host: T): T {
        return host.withActiveExercise(null)
    }

    private fun <T : ExerciseInteractionHost<T>> navigate(
        host: T,
        offset: Int
    ): ExerciseInteractionResult<T> {
        val current = host.activeExercise ?: return ExerciseInteractionResult(host)

        val exercises = host.workout.items.filterIsInstance<ExerciseUiItem>()
        val currentIndex = exercises.indexOfFirst { it.key == current.key }
        val next = exercises.getOrNull(currentIndex + offset)
            ?: return ExerciseInteractionResult(host)

        return ExerciseInteractionResult(
            state = host,
            effect = ExerciseInteractionEffect.LoadExerciseInfo(next.key, next.exerciseId)
        )
    }

    private fun <T : ExerciseInteractionHost<T>> changeQuantity(
        host: T,
        increase: Boolean
    ): T {
        val active = host.activeExercise ?: return host

        val newQuantity = quantityChange(
            type = active.draft.quantityType,
            currentQuantityValue = active.draft.quantityValue,
            increase = increase
        )

        val isDirty = newQuantity != active.original.quantityValue ||
                active.draft.exerciseId != active.original.exerciseId

        return host.withActiveExercise(
            active.copy(
                draft = active.draft.copy(
                    quantityValue = newQuantity,
                    isDirty = isDirty
                )
            )
        )
    }

    private fun <T : ExerciseInteractionHost<T>> reset(host: T): T {
        val active = host.activeExercise ?: return host

        return host.withActiveExercise(
            active.copy(
                draft = active.original.copy()
            )
        )
    }

    private fun <T : ExerciseInteractionHost<T>> exchange(
        host: T,
        newInfo: ExerciseInfoUiModel
    ): T {
        val active = host.activeExercise ?: return host

        val mappedQuantity = mapQuantityValue(
            active.draft.quantityType,
            active.draft.quantityValue,
            newInfo.quantityType
        )

        val isDirty = newInfo.exerciseId != active.original.exerciseId ||
                mappedQuantity != active.original.quantityValue

        val updatedDraft = active.draft.copy(
            exerciseId = newInfo.exerciseId,
            name = newInfo.name,
            image = newInfo.image,
            descriptionMarkdown = newInfo.descriptionMarkdown,
            quantityType = newInfo.quantityType,
            quantityValue = mappedQuantity,
            position = active.draft.position,
            total = active.draft.total,
            isDirty = isDirty
        )

        return host.withActiveExercise(
            active.copy(draft = updatedDraft)
        )
    }

    private fun <T : ExerciseInteractionHost<T>> save(host: T): T {
        val active = host.activeExercise ?: return host

        val updatedItems = host.workout.items.map { item ->
            if (item.key != active.key) return@map item

            val exercise = item as ExerciseUiItem
            exercise.copy(
                exerciseId = active.draft.exerciseId,
                quantityType = active.draft.quantityType,
                quantityValue = active.draft.quantityValue,
                name = active.draft.name,
                image = if (active.draft.image.isEmpty()) exercise.image else active.draft.image,
            )
        }

        return host
            .withWorkout(host.workout.copy(items = updatedItems))
            .withActiveExercise(null)
    }
}