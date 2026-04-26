package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.ChangeQuantity
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Close
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Exchange
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Next
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Open
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Prev
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Reset
import com.pl.myworkoutapp.ui.workouts.ExerciseInteractionAction.Save
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.CloseEditor
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.LoadExerciseForList
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.LoadExerciseForPreview
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.LoadExerciseInfo
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.ResetDraft
import com.pl.myworkoutapp.ui.workouts.WorkoutEditEffect.SaveDraft
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoAction

/**
 * Obsługuje edit concerns:
 *
 * rename
 * reorder
 * add/delete
 * circuit editor
 * save draft
 * reset draft
 *
 * i też deleguje exercise flow do shared reducera.
 *
 * To reducer trybu edycji.
 *
 * Obsługuje:
 *
 * rename
 * reorder
 * add/delete
 * circuit editor
 * draft save/reset
 * active exercise flow (delegated)
 *
 * To jest „duży” reducer.
 */

sealed interface WorkoutEditAction {
    data class Rename(val value: String) : WorkoutEditAction
    data class ChangeDescription(val value: String) : WorkoutEditAction
    data class Move(val from: Int, val to: Int) : WorkoutEditAction

    data object AddExercise : WorkoutEditAction
    data object AddCircuit : WorkoutEditAction
    data class EditCircuit(val key: Int) : WorkoutEditAction
    data class DeleteItem(val key: Int) : WorkoutEditAction
    data class ChangeQuantityOnList(val key: Int, val increase: Boolean) : WorkoutEditAction

    data class ShowExerciseInfo(val key: Int, val exerciseId: ExerciseId) : WorkoutEditAction
    data class ShowLoadedExerciseInfo(val key: Int, val info: ExerciseInfoUiModel) :
        WorkoutEditAction

    data object CloseExerciseInfo : WorkoutEditAction
    data object ExerciseNext : WorkoutEditAction
    data object ExercisePrev : WorkoutEditAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutEditAction
    data object ExerciseReset : WorkoutEditAction
    data object ExerciseSave : WorkoutEditAction
    data class ExercisePicked(val exerciseId: ExerciseId?) : WorkoutEditAction
    data class SelectedExerciseLoaded(
        val context: ExercisePickerContext,
        val exercise: ExerciseUiItem
    ) : WorkoutEditAction

    data class ExerciseExchangeStart(val key: Int, val currentExerciseId: ExerciseId) :
        WorkoutEditAction

    data object SaveCircuitEditor : WorkoutEditAction
    data object CancelCircuitEditor : WorkoutEditAction
    data object DeleteElementConfirm : WorkoutEditAction
    data object DeleteElementCancel : WorkoutEditAction

    data object SaveDraft : WorkoutEditAction // commit draft → view session
    data object ResetDraft : WorkoutEditAction
    data object CloseEditor : WorkoutEditAction
    data object ShowExercisePicker : WorkoutEditAction
    data class ExerciseReplaced(val info: ExerciseInfoUiModel) : WorkoutEditAction
}

fun WorkoutExerciseInfoAction.toWorkoutEditAction(): WorkoutEditAction = when (this) {
    is WorkoutExerciseInfoAction.ChangeQuantity -> WorkoutEditAction.ChangeQuantity(increase)
    WorkoutExerciseInfoAction.CloseExerciseInfo -> WorkoutEditAction.CloseExerciseInfo
    WorkoutExerciseInfoAction.ExerciseNext -> WorkoutEditAction.ExerciseNext
    WorkoutExerciseInfoAction.ExercisePrev -> WorkoutEditAction.ExercisePrev
    WorkoutExerciseInfoAction.ExerciseReset -> WorkoutEditAction.ExerciseReset
    WorkoutExerciseInfoAction.ExerciseSave -> WorkoutEditAction.ExerciseSave
    WorkoutExerciseInfoAction.ShowExercisePicker -> WorkoutEditAction.ShowExercisePicker
}

data class WorkoutEditResult(
    val state: WorkoutEditSession,
    val effect: WorkoutEditEffect? = null,
)

sealed interface WorkoutEditEffect {
    data class LoadExerciseInfo(val key: Int, val exerciseId: ExerciseId) : WorkoutEditEffect
    data class LoadExerciseForList(val context: ExercisePickerContext, val exerciseId: ExerciseId) :
        WorkoutEditEffect

    data class LoadExerciseForPreview(val exerciseId: ExerciseId) : WorkoutEditEffect

    data object SaveDraft : WorkoutEditEffect
    data object ResetDraft : WorkoutEditEffect
    data object CloseEditor : WorkoutEditEffect
}


fun WorkoutEditSession.getExercise(key: Int): ExerciseUiItem =
    requireNotNull(workout.items.find { it.uiKey == key } as? ExerciseUiItem) {
        "ExerciseUiItem not found for key=$key"
    }

fun WorkoutEditSession.getCircuit(key: Int): CircuitUiItem =
    requireNotNull(workout.items.find { it.uiKey == key } as? CircuitUiItem) {
        "CircuitUiItem not found for key=$key"
    }

class WorkoutEditReducer(
    private val exerciseReducer: ExerciseInteractionReducer,
    private val circuitReducer: CircuitEditorDelegate,
) {
    private val TAG = "WorkoutEditReducer"

    fun reduce(
        session: WorkoutEditSession,
        action: CircuitEditorAction
    ): WorkoutEditSession {
        val editableCircuit = session.editableCircuit ?: return session

        return session.copy(
            editableCircuit = circuitReducer.reduce(editableCircuit, action)
        )
    }

    fun reduce(
        session: WorkoutEditSession,
        action: WorkoutEditAction
    ): WorkoutEditResult {
        return when (action) {
            is WorkoutEditAction.ShowExerciseInfo -> WorkoutEditResult(
                state = session,
                effect = LoadExerciseInfo(action.key, action.exerciseId)
            )

            is WorkoutEditAction.ShowLoadedExerciseInfo ->
                delegate(session, Open(action.key, action.info))

            WorkoutEditAction.CloseExerciseInfo ->
                delegate(session, Close)

            WorkoutEditAction.ExerciseNext ->
                delegate(session, Next)

            WorkoutEditAction.ExercisePrev ->
                delegate(session, Prev)

            is WorkoutEditAction.ChangeQuantity ->
                delegate(session, ChangeQuantity(action.increase))

            is WorkoutEditAction.ExerciseReplaced ->
                delegate(session, Exchange(action.info))

            WorkoutEditAction.ExerciseReset ->
                delegate(session, Reset)

            WorkoutEditAction.ExerciseSave ->
                delegate(session, Save)


            is WorkoutEditAction.Rename -> WorkoutEditResult(
                rename(session, action.value)
            )

            is WorkoutEditAction.ChangeDescription -> WorkoutEditResult(
                changeDescription(session, action.value)
            )

            is WorkoutEditAction.Move -> WorkoutEditResult(
                move(session, action.from, action.to)
            )

            //FAB action
            WorkoutEditAction.AddExercise -> WorkoutEditResult(
                state = session.copy(
                    modal = WorkoutEditModal.ExercisePicker(
                        ExercisePickerContext.AddExercise, null
                    )
                )
            )

            //FAB action
            WorkoutEditAction.AddCircuit -> WorkoutEditResult(
                openCircuitEditorNew(session)
            )

            is WorkoutEditAction.EditCircuit -> WorkoutEditResult(
                openCircuitEditorEdit(session, action.key)
            )

            WorkoutEditAction.CancelCircuitEditor -> WorkoutEditResult(
                state = session.copy(
                    editableCircuit = null,
                    editingCircuitItemKey = null,
                )
            )

            WorkoutEditAction.SaveCircuitEditor -> WorkoutEditResult(
                saveCircuitEditor(session)
            )

            is WorkoutEditAction.DeleteItem -> WorkoutEditResult(
                state = session.copy(
                    deletingWorkoutItemKey = action.key,
                    modal = WorkoutEditModal.ConfirmDeleteItem
                )
            )

            WorkoutEditAction.DeleteElementCancel -> WorkoutEditResult(
                state = session.copy(deletingWorkoutItemKey = null, modal = null)
            )

            WorkoutEditAction.DeleteElementConfirm -> WorkoutEditResult(
                deleteWorkoutElement(session)
            )

            is WorkoutEditAction.ExercisePicked ->
                exercisePicked(session, action.exerciseId)

            is WorkoutEditAction.SelectedExerciseLoaded -> WorkoutEditResult(
                exercisePickedLoaded(session, action.context, action.exercise)
            )

            WorkoutEditAction.SaveDraft ->
                WorkoutEditResult(session, SaveDraft)

            WorkoutEditAction.ResetDraft ->
                WorkoutEditResult(session, ResetDraft)

            WorkoutEditAction.CloseEditor ->
                WorkoutEditResult(session, CloseEditor)

            is WorkoutEditAction.ExerciseExchangeStart -> WorkoutEditResult(
                //tryb zmiany ćwiczenia po kliknięciu ikony exchange na ćwiczeniu w liście
                state = session.copy(
                    modal = WorkoutEditModal.ExercisePicker(
                        ExercisePickerContext.ReplaceListItem(action.key),
                        action.currentExerciseId
                    )
                )
            )

            WorkoutEditAction.ShowExercisePicker -> {
                //tryb zmiany ćwiczenia po kliknięciu ikony exchange w podglądzie ćwiczenia
                //val key = session.activeExercise?.key ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        modal = WorkoutEditModal.ExercisePicker(
                            ExercisePickerContext.ReplacePreview,
                            session.activeExercise?.info?.exerciseId
                        )
                    )
                )
            }

            is WorkoutEditAction.ChangeQuantityOnList -> WorkoutEditResult(
                changeQuantityOnList(session, action.key, action.increase)
            )
        }
    }

    private fun delegate(
        session: WorkoutEditSession,
        action: ExerciseInteractionAction
    ): WorkoutEditResult {
        val result = exerciseReducer.reduce(session, action)

        return WorkoutEditResult(
            state = result.state,
            effect = result.effect?.toEditEffect()
        )
    }

    private fun ExerciseInteractionEffect.toEditEffect(): WorkoutEditEffect =
        when (this) {
            is ExerciseInteractionEffect.LoadExerciseInfo ->
                LoadExerciseInfo(key, exerciseId)
        }

    private fun rename(
        session: WorkoutEditSession,
        value: String
    ): WorkoutEditSession {
        return session.copy(
            workout = session.workout.copy(
                workout = session.workout.workout.copy(
                    name = value.asUiText()
                )
            )
        )
    }

    private fun changeDescription(
        session: WorkoutEditSession,
        value: String
    ): WorkoutEditSession {
        return session.copy(
            workout = session.workout.copy(
                workout = session.workout.workout.copy(
                    desc = value.asUiText()
                )
            )
        )
    }

    private fun move(
        session: WorkoutEditSession,
        from: Int,
        to: Int
    ): WorkoutEditSession {
        //TODO - tutaj trzeba sie zastanowic co z timeline + circuit elementami
        val items = session.workout.items.toMutableList()
        val item = items.removeAt(from)
        items.add(to, item)

        return session.copy(
            workout = session.workout.copy(items = items)
        )
    }

    private fun exercisePicked(
        session: WorkoutEditSession,
        exerciseId: ExerciseId?
    ): WorkoutEditResult {
        val modal = session.modal as? WorkoutEditModal.ExercisePicker
            ?: return WorkoutEditResult(session)

        if (exerciseId == null) {
            return WorkoutEditResult(
                state = session.copy(modal = null)
            )
        }

        return when (modal.context) {
            ExercisePickerContext.AddExercise ->
                WorkoutEditResult(
                    state = session.copy(modal = null),
                    effect = LoadExerciseForList(modal.context, exerciseId)
                )

            is ExercisePickerContext.ReplaceListItem -> WorkoutEditResult(
                state = session.copy(modal = null),
                effect = LoadExerciseForList(modal.context, exerciseId)
            )

            is ExercisePickerContext.ReplacePreview ->
                WorkoutEditResult(
                    state = session.copy(modal = null),
                    effect = LoadExerciseForPreview(exerciseId)
                )
        }
    }

    private fun exercisePickedLoaded(
        session: WorkoutEditSession,
        context: ExercisePickerContext,
        exercise: ExerciseUiItem
    ): WorkoutEditSession {
        return when (context) {
            ExercisePickerContext.AddExercise ->
                addExercise(session.copy(modal = null), exercise)

            is ExercisePickerContext.ReplaceListItem ->
                exchangeExercise(session.copy(modal = null), context.key, exercise)

            ExercisePickerContext.ReplacePreview -> {
                Log.e(TAG, "Invalid ReplacePreview in exercisePickedLoaded")
                session
            }
        }
    }

    private fun addExercise(
        session: WorkoutEditSession,
        exercise: ExerciseUiItem
    ): WorkoutEditSession {
        Log.d(TAG, "addExercise : ${exercise.exerciseId}")
        val nextKey = (session.workout.items.maxOfOrNull { it.uiKey } ?: 0) + 1

        val exe = exercise.copy(
            uiKey = nextKey,
            //TODO - tutaj trzeba sie zastanowic co z timeline + circuit elementami
            timeline = listOf(TimeLineItemType.End(Color.Red)),
            depth = 0, //TODO - to wymaga zaimplementowania jeszcze
        )

        val newItems = session.workout.items + exe

        return session.copy(
            workout = session.workout.copy(items = newItems),
            scrollToIdx = newItems.lastIndex,
        )
    }

    private fun exchangeExercise(
        session: WorkoutEditSession,
        key: Int, newExercise: ExerciseUiItem
    ): WorkoutEditSession {
        val existingExercise = session.getExercise(key)
        val newQuantityValue = mapQuantityValue(
            existingExercise.quantityType,
            existingExercise.quantityValue,
            newExercise.quantityType
        )
        val newExe = newExercise.copy(
            quantityValue = newQuantityValue,
            uiKey = existingExercise.uiKey,
            timeline = existingExercise.timeline,
            depth = existingExercise.depth,
        )

        return session.copy(
            workout = session.workout.copy(
                items = session.workout.items.map {
                    if (it.uiKey != existingExercise.uiKey) it else newExe
                }
            ),
        )
    }

    private fun openCircuitEditorNew(
        session: WorkoutEditSession,
    ): WorkoutEditSession {
        val editableCircuit = CircuitEditorUiState(isNew = true)
        return session.copy(
            editableCircuit = editableCircuit.copy(
                isValid = circuitReducer.validate(
                    editableCircuit
                )
            ),
            editingCircuitItemKey = null,
        )
    }

    private fun openCircuitEditorEdit(
        session: WorkoutEditSession,
        key: Int
    ): WorkoutEditSession {
        val circuit = session.getCircuit(key)
        val editableCircuit = circuit.toCircuitEditorUiState()
        return session.copy(
            editableCircuit = editableCircuit.copy(
                isValid = circuitReducer.validate(
                    editableCircuit
                )
            ),
            editingCircuitItemKey = key,
        )
    }

    private fun saveCircuitEditor(
        session: WorkoutEditSession,
    ): WorkoutEditSession {
        val current = session.editableCircuit ?: return session
        return if (session.editingCircuitItemKey == null) {
            //NEW CIRCUIT
            val newWorkout = addCircuit(session.workout, current)
            session.copy(
                workout = newWorkout,
                scrollToIdx = newWorkout.items.lastIndex,
                editableCircuit = null,
                editingCircuitItemKey = null
            )
        } else {
            val newWorkout = modifyCircuit(session.workout, session.editingCircuitItemKey, current)
            session.copy(
                workout = newWorkout,
                editableCircuit = null,
                editingCircuitItemKey = null
            )
        }
    }

    private fun addCircuit(
        state: WorkoutWithExercisesUiModel,
        circuit: CircuitEditorUiState
    ): WorkoutWithExercisesUiModel {
        Log.d(TAG, "addCircuit : $circuit")
        val nextKey = (state.items.maxOfOrNull { it.uiKey } ?: 0) + 1
        return state.copy(
            items = state.items + CircuitUiItem(
                phase = circuit.phase,
                structure = circuit.toStructure(),
                title = circuit.name,
                uiKey = nextKey,
                timeline = listOf(TimeLineItemType.End(Color.Red)),
                depth = 0, //TODO - to wymaga zaimplementowania jeszcze
            )
        )
    }

    private fun modifyCircuit(
        state: WorkoutWithExercisesUiModel,
        circuitKey: Int, modified: CircuitEditorUiState
    ): WorkoutWithExercisesUiModel {
        val currentCircuit = state.items.first { it.uiKey == circuitKey } as CircuitUiItem
        return state.copy(
            items = state.items.map {
                if (it.uiKey != circuitKey) it else currentCircuit.copy(
                    phase = modified.phase,
                    structure = modified.toStructure(),
                    title = modified.name,
                )
            },
        )
    }

    private fun deleteWorkoutElement(session: WorkoutEditSession): WorkoutEditSession {
        val toDeleteKey = session.deletingWorkoutItemKey ?: return session
        return session.copy(
            workout = session.workout.copy(
                items = session.workout.items.filter { it.uiKey != toDeleteKey }
            ),
            deletingWorkoutItemKey = null,
            modal = null,
        )
    }

    private fun changeQuantityOnList(
        session: WorkoutEditSession,
        key: Int,
        increase: Boolean
    ): WorkoutEditSession {
        val exercise = session.getExercise(key)
        val newQuantityValue = quantityChange(
            type = exercise.quantityType,
            currentQuantityValue = exercise.quantityValue,
            increase = increase
        )
        return session.copy(
            workout = session.workout.copy(
                items = session.workout.items.map {
                    if (it.uiKey != exercise.uiKey) it else exercise.copy(
                        quantityValue = newQuantityValue,
                    )
                },
            ),
        )
    }
}