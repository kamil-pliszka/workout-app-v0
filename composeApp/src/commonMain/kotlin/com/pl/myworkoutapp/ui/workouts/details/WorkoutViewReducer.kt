package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoAction
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesAction
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.ChangeQuantity
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Close
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Exchange
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Next
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Open
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Prev
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Reset
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Save
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.ExchangeExercise
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.LoadExerciseInfo
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.OpenEditor
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.ResetWorkout
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.SaveWorkout
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewEffect.StartWorkout

/**
 * To reducer trybu podglądu. Obsługuje tylko flow ekranu view:
 *
 * open exercise info
 * quick changes
 * save/reset quick changes
 * start workout
 * open editor
 *
 * Nie zna:
 *
 * circuit editor
 * reorder
 * delete item
 * draft editing
 *
 * To jest ważne: view reducer nie zna edytora.
 */

sealed interface WorkoutViewAction {
    data class ShowExerciseInfo(val key: Int, val exerciseId: ExerciseId) : WorkoutViewAction
    data class ShowLoadedExerciseInfo(val key: Int, val info: ExerciseInfoUiModel) :
        WorkoutViewAction

    data class ExerciseReplaced(val info: ExerciseInfoUiModel) : WorkoutViewAction
    data object CloseExerciseInfo : WorkoutViewAction

    data object ExerciseNext : WorkoutViewAction
    data object ExercisePrev : WorkoutViewAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutViewAction
    data object ExerciseReset : WorkoutViewAction
    data object ExerciseSave : WorkoutViewAction
    data object ShowExercisePicker : WorkoutViewAction
    data class ExercisePicked(val exerciseId: ExerciseId?) : WorkoutViewAction

    data object SaveWorkout : WorkoutViewAction // persist current workout
    data object ResetWorkout : WorkoutViewAction
    data object StartWorkout : WorkoutViewAction
    data object OpenEditor : WorkoutViewAction
    data object OnBack : WorkoutViewAction
    data object DeleteRequest : WorkoutViewAction
    data object ResetRequest : WorkoutViewAction
    data object TuneRequest : WorkoutViewAction
    data object DeleteConfirm : WorkoutViewAction
    data object DeleteCancel : WorkoutViewAction
}

fun WorkoutExerciseInfoAction.toWorkoutViewAction(): WorkoutViewAction = when (this) {
    is WorkoutExerciseInfoAction.ChangeQuantity -> WorkoutViewAction.ChangeQuantity(increase)
    WorkoutExerciseInfoAction.CloseExerciseInfo -> WorkoutViewAction.CloseExerciseInfo
    WorkoutExerciseInfoAction.ExerciseNext -> WorkoutViewAction.ExerciseNext
    WorkoutExerciseInfoAction.ExercisePrev -> WorkoutViewAction.ExercisePrev
    WorkoutExerciseInfoAction.ExerciseReset -> WorkoutViewAction.ExerciseReset
    WorkoutExerciseInfoAction.ExerciseSave -> WorkoutViewAction.ExerciseSave
    WorkoutExerciseInfoAction.ShowExercisePicker -> WorkoutViewAction.ShowExercisePicker
}

fun WorkoutWithExercisesAction.toWorkoutViewAction(): WorkoutViewAction = when (this) {
    WorkoutWithExercisesAction.OnBack -> WorkoutViewAction.OnBack
    WorkoutWithExercisesAction.OnDeleteRequest -> WorkoutViewAction.DeleteRequest
    WorkoutWithExercisesAction.OnResetRequest -> WorkoutViewAction.ResetRequest
    WorkoutWithExercisesAction.OnOpenEditor -> WorkoutViewAction.OpenEditor
    WorkoutWithExercisesAction.OnTuneRequest -> WorkoutViewAction.TuneRequest
    is WorkoutWithExercisesAction.ShowExerciseInfo -> WorkoutViewAction.ShowExerciseInfo(
        key,
        exerciseId
    )
}


data class WorkoutViewResult(
    val state: WorkoutViewSession,
    val effect: WorkoutViewEffect? = null
)

sealed interface WorkoutViewEffect {
    data class LoadExerciseInfo(val key: Int, val exerciseId: ExerciseId) : WorkoutViewEffect
    data class ExchangeExercise(val exerciseId: ExerciseId) : WorkoutViewEffect
    data object SaveWorkout : WorkoutViewEffect
    data object ResetWorkout : WorkoutViewEffect
    data object StartWorkout : WorkoutViewEffect
    data object OpenEditor : WorkoutViewEffect
    data object DeleteWorkout : WorkoutViewEffect
}


class WorkoutViewReducer(
    private val exerciseReducer: ExerciseInteractionReducer
) {

    fun reduce(
        session: WorkoutViewSession,
        action: WorkoutViewAction
    ): WorkoutViewResult {
        return when (action) {
            is WorkoutViewAction.ShowExerciseInfo ->
                WorkoutViewResult(
                    state = session,
                    effect = LoadExerciseInfo(
                        key = action.key,
                        exerciseId = action.exerciseId
                    )
                )

            is WorkoutViewAction.ShowLoadedExerciseInfo ->
                delegate(session, Open(action.key, action.info))

            WorkoutViewAction.CloseExerciseInfo ->
                delegate(session, Close)

            WorkoutViewAction.ExerciseNext ->
                delegate(session, Next)

            WorkoutViewAction.ExercisePrev ->
                delegate(session, Prev)

            is WorkoutViewAction.ChangeQuantity ->
                delegate(session, ChangeQuantity(action.increase))

            is WorkoutViewAction.ExerciseReplaced ->
                delegate(session, Exchange(action.info))

            WorkoutViewAction.ExerciseReset ->
                delegate(session, Reset)

            WorkoutViewAction.ExerciseSave ->
                delegate(session.copy(hasUnsavedChanges = true), Save)
            //Exercise info end


            WorkoutViewAction.ShowExercisePicker ->
                WorkoutViewResult(
                    state = session.copy(
                        modal = WorkoutViewModal.ExercisePicker
                    )
                )

            is WorkoutViewAction.ExercisePicked ->
                onExercisePicked(session, action.exerciseId)

            WorkoutViewAction.SaveWorkout ->
                WorkoutViewResult(session, SaveWorkout)

            WorkoutViewAction.ResetWorkout ->
                WorkoutViewResult(session, ResetWorkout)

            WorkoutViewAction.StartWorkout ->
                WorkoutViewResult(session, StartWorkout)

            WorkoutViewAction.OpenEditor ->
                WorkoutViewResult(session, OpenEditor)


            WorkoutViewAction.OnBack -> WorkoutViewResult(session)
            WorkoutViewAction.DeleteRequest -> WorkoutViewResult(
                state = session.copy(
                    modal = WorkoutViewModal.ConfirmDelete
                )
            )

            WorkoutViewAction.ResetRequest -> WorkoutViewResult(
                state = session.copy(
                    modal = WorkoutViewModal.ConfirmReset
                )
            )

            WorkoutViewAction.DeleteCancel -> WorkoutViewResult(
                state = session.copy(modal = null)
            )

            WorkoutViewAction.DeleteConfirm ->
                WorkoutViewResult(session, WorkoutViewEffect.DeleteWorkout)

            WorkoutViewAction.TuneRequest -> TODO() //celowy wyjątek
        }
    }

    private fun onExercisePicked(
        session: WorkoutViewSession,
        exerciseId: ExerciseId?
    ): WorkoutViewResult {
        val cleared = session.copy(modal = null)

        return if (exerciseId == null) {
            WorkoutViewResult(cleared)
        } else {
            WorkoutViewResult(
                state = cleared,
                effect = ExchangeExercise(exerciseId)
            )
        }
    }

    private fun delegate(
        session: WorkoutViewSession,
        action: ExerciseInteractionAction
    ): WorkoutViewResult {
        val result = exerciseReducer.reduce(session, action)

        return WorkoutViewResult(
            state = result.state,
            effect = result.effect?.toViewEffect()
        )
    }

    private fun ExerciseInteractionEffect.toViewEffect(): WorkoutViewEffect {
        return when (this) {
            is ExerciseInteractionEffect.LoadExerciseInfo ->
                LoadExerciseInfo(
                    key = key,
                    exerciseId = exerciseId
                )
        }
    }
}