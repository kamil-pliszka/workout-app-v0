package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.workouts.*
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoAction
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.ChangeQuantity
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Close
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Exchange
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Next
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Open
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Prev
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Reset
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionAction.Save
import com.pl.myworkoutapp.ui.workouts.details.ExercisePickerContext.*
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.CloseEditor
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.LoadExerciseForList
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.LoadExerciseForPreview
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.LoadExerciseInfo
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.ResetDraft
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditEffect.SaveDraft
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditModal.*
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeMutation
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeMutationHandler

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
    data class Drop(val event: DragDropEvent) : WorkoutEditAction

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
    data class DeleteElementConfirm(val key: Int) : WorkoutEditAction
    data object DeleteElementCancel : WorkoutEditAction

    data object SaveDraft : WorkoutEditAction // commit draft → view session
    data object ResetDraft : WorkoutEditAction
    data object CloseEditor : WorkoutEditAction
    data object ShowExercisePicker : WorkoutEditAction
    data class ExerciseReplaced(val info: ExerciseInfoUiModel) : WorkoutEditAction

    data object OpenMetadataEditor : WorkoutEditAction
    data object CancelMetadataEditor : WorkoutEditAction
    data object SaveMetadataEditor : WorkoutEditAction

    data class UpdateMetadataName(val value: String) : WorkoutEditAction
    data class UpdateMetadataDescription(val value: String) : WorkoutEditAction
    data class UpdateMetadataImage(val value: UiImage) : WorkoutEditAction
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
    data class ScrollTo(val index: Int) : WorkoutEditEffect
    data object Vibration : WorkoutEditEffect

    data object SaveDraft : WorkoutEditEffect
    data object ResetDraft : WorkoutEditEffect
    data object CloseEditor : WorkoutEditEffect
}


fun WorkoutEditSession.getExercise(key: Int): ExerciseUiItem =
    requireNotNull(workout.items.find { it.key == key } as? ExerciseUiItem) {
        "ExerciseUiItem not found for key=$key"
    }

fun WorkoutEditSession.getCircuit(key: Int): CircuitUiItem =
    requireNotNull(workout.items.find { it.key == key } as? CircuitUiItem) {
        "CircuitUiItem not found for key=$key"
    }

class WorkoutEditReducer(
    private val exerciseReducer: ExerciseInteractionReducer,
    private val circuitReducer: CircuitEditorDelegate,
    private val workoutTreeMutationHandler: WorkoutTreeMutationHandler
) {
    @Suppress("PrivatePropertyName")
    private val TAG = "WorkoutEditReducer"

    fun reduce(
        session: WorkoutEditSession,
        action: CircuitEditorAction
    ): WorkoutEditResult {
        val editableCircuit = session.editableCircuit ?: return WorkoutEditResult(session)

        return WorkoutEditResult(
            session.copy(
                editableCircuit = circuitReducer.reduce(editableCircuit, action)
            )
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

            is WorkoutEditAction.Drop ->
                drop(session, action.event)

            //FAB action
            WorkoutEditAction.AddExercise -> WorkoutEditResult(
                state = session.copy(
                    modal = ExercisePicker(
                        AddExercise, null
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

            WorkoutEditAction.SaveCircuitEditor ->
                saveCircuitEditor(session)

            is WorkoutEditAction.DeleteItem -> WorkoutEditResult(
                state = session.copy(
                    modal = ConfirmDeleteItem(action.key)
                )
            )

            WorkoutEditAction.DeleteElementCancel -> WorkoutEditResult(
                state = session.copy(modal = null)
            )

            is WorkoutEditAction.DeleteElementConfirm -> WorkoutEditResult(
                deleteWorkoutElement(session, action.key)
            )

            is WorkoutEditAction.ExercisePicked ->
                exercisePicked(session, action.exerciseId)

            is WorkoutEditAction.SelectedExerciseLoaded ->
                exercisePickedLoaded(session, action.context, action.exercise)

            WorkoutEditAction.SaveDraft ->
                WorkoutEditResult(session, SaveDraft)

            WorkoutEditAction.ResetDraft ->
                WorkoutEditResult(session, ResetDraft)

            WorkoutEditAction.CloseEditor ->
                WorkoutEditResult(session, CloseEditor)

            is WorkoutEditAction.ExerciseExchangeStart -> WorkoutEditResult(
                //tryb zmiany ćwiczenia po kliknięciu ikony exchange na ćwiczeniu w liście
                state = session.copy(
                    modal = ExercisePicker(
                        ReplaceListItem(action.key),
                        action.currentExerciseId
                    )
                )
            )

            WorkoutEditAction.ShowExercisePicker -> {
                //tryb zmiany ćwiczenia po kliknięciu ikony exchange w podglądzie ćwiczenia
                //val key = session.activeExercise?.key ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        modal = ExercisePicker(
                            ReplacePreview,
                            session.activeExercise?.draft?.exerciseId
                        )
                    )
                )
            }

            is WorkoutEditAction.ChangeQuantityOnList -> WorkoutEditResult(
                changeQuantityOnList(session, action.key, action.increase)
            )

            WorkoutEditAction.OpenMetadataEditor -> {
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = WorkoutMetadataDraft(
                            image = session.workout.workout.image,
                            name = session.workout.workout.name,
                            description = session.workout.workout.desc,
                        )
                    )
                )
            }

            WorkoutEditAction.CancelMetadataEditor -> {
                WorkoutEditResult(
                    state = session.copy(editableMetadata = null)
                )
            }

            WorkoutEditAction.SaveMetadataEditor -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        workout = session.workout.copy(
                            workout = session.workout.workout.copy(
                                image = draft.image,
                                name = draft.name,
                                desc = draft.description,
                            )
                        ),
                        editableMetadata = null,
                    )
                )
            }

            is WorkoutEditAction.UpdateMetadataName -> {
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = session.editableMetadata?.copy(name = action.value.asUiText())
                    )
                )
            }

            is WorkoutEditAction.UpdateMetadataDescription -> {
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = session.editableMetadata?.copy(description = action.value.asUiText())
                    )
                )
            }

            is WorkoutEditAction.UpdateMetadataImage -> {
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = session.editableMetadata?.copy(image = action.value)
                    )
                )
            }
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

    private fun drop(session: WorkoutEditSession, event: DragDropEvent): WorkoutEditResult {
        Log.d(TAG, "Drop event: $event")
        val workout = workoutTreeMutationHandler.apply(
            session.workout,
            WorkoutTreeMutation.Move(
                event.draggedKey,
                event.targetKey,
                event.position
            )
        )
        val movedItemIndex = workout.items.indexOfFirst { it.key == event.draggedKey }
        return WorkoutEditResult(
            session.copy(
                workout = workout
            ),
            if (movedItemIndex >= 0) WorkoutEditEffect.ScrollTo(movedItemIndex) else null
        )
    }

    private fun exercisePicked(
        session: WorkoutEditSession,
        exerciseId: ExerciseId?
    ): WorkoutEditResult {
        val modal = session.modal as? ExercisePicker
            ?: return WorkoutEditResult(session)

        if (exerciseId == null) {
            return WorkoutEditResult(
                state = session.copy(modal = null)
            )
        }

        return when (modal.context) {
            AddExercise ->
                WorkoutEditResult(
                    state = session.copy(modal = null),
                    effect = LoadExerciseForList(modal.context, exerciseId)
                )

            is ReplaceListItem -> WorkoutEditResult(
                state = session.copy(modal = null),
                effect = LoadExerciseForList(modal.context, exerciseId)
            )

            is ReplacePreview ->
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
    ): WorkoutEditResult {
        return when (context) {
            AddExercise ->
                addExercise(session.copy(modal = null), exercise)

            is ReplaceListItem ->
                exchangeExercise(session.copy(modal = null), context.key, exercise)

            ReplacePreview -> {
                Log.e(TAG, "Invalid ReplacePreview in exercisePickedLoaded")
                WorkoutEditResult(session)
            }
        }
    }

    private fun addExercise(
        session: WorkoutEditSession,
        exercise: ExerciseUiItem
    ): WorkoutEditResult {
        Log.d(TAG, "addExercise : ${exercise.exerciseId}")
        val nextKey = (session.workout.items.maxOfOrNull { it.key } ?: 0) + 1
        val exe = exercise.copy(key = nextKey)

        val workout = workoutTreeMutationHandler.apply(
            session.workout,
            WorkoutTreeMutation.InsertExercise(
                exercise = exe,
                targetKey = null,//root level
                position = DropPosition.BEFORE
            )
        )
        return WorkoutEditResult(
            session.copy(
                workout = workout
            ),
            if (workout.items.lastIndex >= 0) WorkoutEditEffect.ScrollTo(workout.items.lastIndex) else null
        )
    }

    private fun exchangeExercise(
        session: WorkoutEditSession,
        key: Int, newExercise: ExerciseUiItem
    ): WorkoutEditResult {
        val existingExercise = session.getExercise(key)
        val newQuantityValue = mapQuantityValue(
            existingExercise.quantityType,
            existingExercise.quantityValue,
            newExercise.quantityType
        )
        val newExe = newExercise.copy(
            quantityValue = newQuantityValue,
            key = existingExercise.key,
            timeline = existingExercise.timeline,
            depth = existingExercise.depth,
        )

        val workout = workoutTreeMutationHandler.apply(
            session.workout,
            WorkoutTreeMutation.ReplaceExercise(
                key = newExe.key,
                newExercise = newExe
            )
        )
        return WorkoutEditResult(
            session.copy(workout = workout)
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
    ): WorkoutEditResult {
        val current = session.editableCircuit ?: return WorkoutEditResult(session)
        return if (session.editingCircuitItemKey == null) {
            //NEW CIRCUIT
            addCircuit(session, current)
        } else {
            val newWorkout = modifyCircuit(session.workout, session.editingCircuitItemKey, current)
            WorkoutEditResult(
                session.copy(
                    workout = newWorkout,
                    editableCircuit = null,
                    editingCircuitItemKey = null
                )
            )
        }
    }

    private fun addCircuit(
        session: WorkoutEditSession,
        circuit: CircuitEditorUiState
    ): WorkoutEditResult {
        Log.d(TAG, "addCircuit : $circuit")
        val nextKey = (session.workout.items.maxOfOrNull { it.key } ?: 0) + 1
        val circuit = CircuitUiItem(
            phase = circuit.phase,
            structure = circuit.toStructure(),
            title = circuit.name,
            key = nextKey,
        )
        val workout = workoutTreeMutationHandler.apply(
            session.workout,
            WorkoutTreeMutation.InsertCircuit(
                circuit = circuit,
                targetKey = null,//root level
                position = DropPosition.BEFORE
            )
        )
        return WorkoutEditResult(
            session.copy(
                workout = workout,
                editableCircuit = null,
                editingCircuitItemKey = null
            ),
            if (workout.items.lastIndex >= 0) WorkoutEditEffect.ScrollTo(workout.items.lastIndex) else null
        )
    }

    private fun modifyCircuit(
        state: WorkoutWithExercisesUiModel,
        circuitKey: Int, modified: CircuitEditorUiState
    ): WorkoutWithExercisesUiModel {
        val currentCircuit = state.items.first { it.key == circuitKey } as CircuitUiItem
        return state.copy(
            items = state.items.map {
                if (it.key != circuitKey) it else currentCircuit.copy(
                    phase = modified.phase,
                    structure = modified.toStructure(),
                    title = modified.name,
                )
            },
        )
    }

    private fun deleteWorkoutElement(
        session: WorkoutEditSession,
        toDeleteKey: Int
    ): WorkoutEditSession {
        val workout = workoutTreeMutationHandler.apply(
            session.workout,
            WorkoutTreeMutation.Delete(
                key = toDeleteKey,
            )
        )

        return session.copy(
            workout = workout,
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
                    if (it.key != exercise.key) it else exercise.copy(
                        quantityValue = newQuantityValue,
                    )
                },
            ),
        )
    }
}