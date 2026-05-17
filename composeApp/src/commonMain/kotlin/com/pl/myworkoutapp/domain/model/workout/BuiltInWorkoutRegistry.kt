package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.core.associateByUnique
import com.pl.myworkoutapp.domain.model.workout.builtin.*

object BuiltInWorkoutRegistry {
    private val BUILT_INS = listOf(
        AbsWorkouts.ALL(),
        TabataWorkouts.ALL(),
        LegsWorkouts.ALL(),
        TestWorkouts.ALL(),
    ).flatten().associateByUnique { it.id.toBuiltInWorkoutId() }

    fun get(id: BuiltInWorkoutId) = BUILT_INS[id] ?: error("Missing built-in workout: $id")

    fun getAllId(): Set<BuiltInWorkoutId> = BUILT_INS.keys

    fun getAll(): List<BuiltInWorkout> = BUILT_INS.values.toList()

    init {
        val missing = BuiltInWorkoutId.entries - BUILT_INS.keys
        require(missing.isEmpty()) {
            "Missing built-in workout: $missing"
        }
    }
}