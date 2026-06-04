package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.common.UiImage
import com.pl.myworkoutapp.ui.common.UiText

/**
 * UI projection ćwiczenia.
 * Odpowiada za:
 * dane potrzebne do renderowania exercise card/view
 * Powinien zawierać:
 * title
 * image
 * reps
 * duration
 * muscle groups
 * itd.
 * Nie powinien exposeować całego modelu domenowego do UI.
 */
data class UiExercise (
    val exerciseId: ExerciseId,
    val title: UiText,
    val image: UiImage,
    val quantityLabel: UiText,
    //val muscleGroups: XXX,
)