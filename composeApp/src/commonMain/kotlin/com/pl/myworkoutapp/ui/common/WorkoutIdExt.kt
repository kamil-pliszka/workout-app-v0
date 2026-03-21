package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId


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
