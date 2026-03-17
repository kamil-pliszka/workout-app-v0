package com.pl.myworkoutapp.domain.model.workout

import kotlin.jvm.JvmInline

sealed interface WorkoutId {
    @JvmInline
    value class BuiltIn(val id: BuiltInWorkoutId) : WorkoutId
    @JvmInline
    value class Custom(val id: Int) : WorkoutId
}

fun Int.asWorkoutId(): WorkoutId.Custom = WorkoutId.Custom(this)

fun BuiltInWorkoutId.asWorkoutId(): WorkoutId.BuiltIn = WorkoutId.BuiltIn(this)

fun WorkoutId.Custom.toInt() = this.id

fun WorkoutId.BuiltIn.toBuiltInWorkoutId() = this.id