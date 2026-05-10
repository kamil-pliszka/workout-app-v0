package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.ui.common.UiImage
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.workouts.details.ExercisePickerContext.AddExercise
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditModal.ExercisePicker

sealed interface MetadataAction {
    data object OpenMetadataEditor : MetadataAction
    data object CancelMetadataEditor : MetadataAction
    data object SaveMetadataEditor : MetadataAction
    data class UpdateMetadataName(val value: String) : MetadataAction
    data class UpdateMetadataDescription(val value: String) : MetadataAction
    data class UpdateMetadataImage(val value: UiImage) : MetadataAction
    data object RemoveMetadataImage : MetadataAction
}

class WorkoutMetadataEditorReducer {

    fun reduce(
        session: WorkoutEditSession,
        action: MetadataAction
    ): WorkoutEditResult {
        return when (action) {
            MetadataAction.OpenMetadataEditor -> {
                WorkoutEditResult(
                    state = session,
                    //obsługiwane jako effect, ponieważ toMetadataDraft jest suspend
                    effect = WorkoutEditEffect.OpenMetadataEditor
                )
            }

            MetadataAction.CancelMetadataEditor -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                if (draft.creationMode) {
                    WorkoutEditResult(
                        state = session,
                        effect = WorkoutEditEffect.AbortWorkoutCreation
                    )
                } else {
                    WorkoutEditResult(
                        state = session.copy(editableMetadata = null)
                    )
                }
            }

            MetadataAction.SaveMetadataEditor -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                val validated = validateMetadata(draft).copy(
                    touchedFields = WorkoutMetadataField.entries.toSet()
                )
                if (!validated.isValid) return WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = validated
                    )
                )

                WorkoutEditResult(
                    state = session.copy(
                        workout = session.workout.copy(
                            workout = session.workout.workout.copy(
                                image = validated.image,
                                name = validated.name.asUiText(),
                                desc = validated.description.asUiText(),
                            )
                        ),
                        editableMetadata = null,
                        //w trybie tworzenia nowego workout od razu idziemy do dodawania ćwiczen
                        modal = if (validated.creationMode)
                            ExercisePicker(AddExercise, null)
                        else null
                    )
                )
            }

            is MetadataAction.UpdateMetadataName -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = validateMetadata(
                            draft.copy(
                                name = action.value,
                                touchedFields = draft.touchedFields + WorkoutMetadataField.NAME
                            )
                        )
                    )
                )
            }

            is MetadataAction.UpdateMetadataDescription -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = validateMetadata(
                            draft.copy(
                                description = action.value,
                                touchedFields = draft.touchedFields + WorkoutMetadataField.DESCRIPTION
                            )
                        )
                    )
                )
            }

            is MetadataAction.UpdateMetadataImage -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = validateMetadata(
                            draft.copy(
                                image = action.value,
                                imageChanged = true,
                                touchedFields = draft.touchedFields + WorkoutMetadataField.IMAGE
                            )
                        )
                    )
                )
            }

            is MetadataAction.RemoveMetadataImage -> {
                val draft = session.editableMetadata ?: return WorkoutEditResult(session)
                WorkoutEditResult(
                    state = session.copy(
                        editableMetadata = validateMetadata(
                            draft.copy(
                                image = UiImage.Empty,
                                imageChanged = true,
                                touchedFields = draft.touchedFields + WorkoutMetadataField.IMAGE
                            )
                        )
                    )
                )
            }
        }
    }

    private fun validateMetadata(state: WorkoutMetadataDraft): WorkoutMetadataDraft {
        val errors = state.validate()
        return state.copy(
            errors = errors,
            isValid = errors.isEmpty(),
        )
    }


}