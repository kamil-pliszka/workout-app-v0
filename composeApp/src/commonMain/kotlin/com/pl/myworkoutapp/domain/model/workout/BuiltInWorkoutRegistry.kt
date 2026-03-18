package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.core.associateByUnique
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts
import com.pl.myworkoutapp.domain.model.workout.builtin.LegsWorkouts
import com.pl.myworkoutapp.domain.model.workout.builtin.TabataWorkouts

object BuiltInWorkoutRegistry {
    private val BUILT_INS = listOf(
        AbsWorkouts.ALL(),
        TabataWorkouts.ALL(),
        LegsWorkouts.ALL(),
    ).flatten().associateByUnique { it.id.toBuiltInWorkoutId() }

    fun get(id: BuiltInWorkoutId) = BUILT_INS[id] ?: error("Missing built-in workout: $id")

    fun getAllId(): Set<BuiltInWorkoutId> = BUILT_INS.keys


    init {
        val missing = BuiltInWorkoutId.entries - BUILT_INS.keys
        require(missing.isEmpty()) {
            "Missing built-in workout: $missing"
        }
    }
}