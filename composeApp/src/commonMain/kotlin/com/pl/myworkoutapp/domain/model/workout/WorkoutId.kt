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


private const val BUILTIN_PREFIX = "BuiltIn:"
private const val CUSTOM_PREFIX = "Custom:"

fun String.toWorkoutIdOrNull(): WorkoutId? = when {
    startsWith(BUILTIN_PREFIX) -> {
        val name = removePrefix(BUILTIN_PREFIX)
        BuiltInWorkoutId.entries.find { it.name == name }?.asWorkoutId()
    }
    startsWith(CUSTOM_PREFIX) -> {
        removePrefix(CUSTOM_PREFIX).toIntOrNull()?.asWorkoutId()
    }
    else -> null
}

fun WorkoutId.asString() = when(this) {
    is WorkoutId.BuiltIn -> BUILTIN_PREFIX + this.id.name
    is WorkoutId.Custom -> CUSTOM_PREFIX + this.id
}
