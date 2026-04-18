package com.pl.myworkoutapp.domain.model.exercise

import kotlin.jvm.JvmInline

sealed interface ExerciseId {
    @JvmInline
    value class BuiltIn(val id: BuiltInExerciseId) : ExerciseId
    @JvmInline
    value class Custom(val id: Long) : ExerciseId {
        companion object {
            val NEW = Custom(0)
        }
        fun isNew() = this == NEW
    }
}

fun Long.asExerciseId(): ExerciseId.Custom = ExerciseId.Custom(this)

fun BuiltInExerciseId.asExerciseId(): ExerciseId.BuiltIn = ExerciseId.BuiltIn(this)

fun ExerciseId.Custom.toLong() = this.id

fun ExerciseId.BuiltIn.toBuiltInExerciseId() = this.id

private const val BUILTIN_PREFIX = "BuiltInExe:"
private const val CUSTOM_PREFIX = "CustomExe:"

fun String.toExerciseIdOrNull(): ExerciseId? = when {
    startsWith(BUILTIN_PREFIX) -> {
        val name = removePrefix(BUILTIN_PREFIX)
        BuiltInExerciseId.entries.find { it.name == name }?.asExerciseId()
    }
    startsWith(CUSTOM_PREFIX) -> {
        removePrefix(CUSTOM_PREFIX).toLongOrNull()?.asExerciseId()
    }
    else -> null
}

fun ExerciseId.asString() = when(this) {
    is ExerciseId.BuiltIn -> BUILTIN_PREFIX + this.id.name
    is ExerciseId.Custom -> CUSTOM_PREFIX + this.id
}
