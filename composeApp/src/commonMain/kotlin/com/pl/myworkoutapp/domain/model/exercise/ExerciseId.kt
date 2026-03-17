package com.pl.myworkoutapp.domain.model.exercise

import kotlin.jvm.JvmInline

sealed interface ExerciseId {
    @JvmInline
    value class BuiltIn(val id: BuiltInExerciseId) : ExerciseId
    @JvmInline
    value class Custom(val id: Int) : ExerciseId
}

fun Int.asExerciseId(): ExerciseId.Custom = ExerciseId.Custom(this)

fun BuiltInExerciseId.asExerciseId(): ExerciseId.BuiltIn = ExerciseId.BuiltIn(this)

fun ExerciseId.Custom.toInt() = this.id

fun ExerciseId.BuiltIn.toBuiltInExerciseId() = this.id
