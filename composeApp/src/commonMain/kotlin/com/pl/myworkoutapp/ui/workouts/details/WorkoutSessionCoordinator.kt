package com.pl.myworkoutapp.ui.workouts.details

class WorkoutSessionCoordinator {
    fun openEditor(view: WorkoutViewSession): WorkoutEditSession {
        return WorkoutEditSession(
            original = view.workout,
            workout = view.workout.copy()
        )
    }

    fun saveEditor(edit: WorkoutEditSession): WorkoutViewSession {
        return WorkoutViewSession(
            workout = edit.workout,
            hasUnsavedChanges = true
        )
    }

    fun closeEditor(edit: WorkoutEditSession): WorkoutViewSession {
        return WorkoutViewSession(
            workout = edit.original,
            hasUnsavedChanges = edit.workout.creationMode
        )
    }

    fun resetEditor(edit: WorkoutEditSession): WorkoutEditSession {
        return edit.copy(
            workout = edit.original.copy()
        )
    }
}