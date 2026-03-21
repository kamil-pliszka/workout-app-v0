package com.pl.myworkoutapp.domain.model.plan

import com.pl.myworkoutapp.core.associateByUnique
import com.pl.myworkoutapp.domain.model.plan.builtin.BellyFatBurnPlan
import com.pl.myworkoutapp.domain.model.plan.builtin.RockSolidAbsPlan
import com.pl.myworkoutapp.domain.model.plan.builtin.SixPackAbsPlan

object BuiltInTrainingPlansRegistry {
    private val BUILT_INS = listOf(
        BellyFatBurnPlan,
        RockSolidAbsPlan,
        SixPackAbsPlan,
    ).associateByUnique { it.id }

    fun get(id: BuiltInTrainingPlanId) = BUILT_INS[id] ?: error("Missing built-in training plan: $id")

    fun getAllId(): Set<BuiltInTrainingPlanId> = BUILT_INS.keys

    fun getAll() : List<TrainingPlan> = BUILT_INS.values.toList()


    init {
        val missing = BuiltInTrainingPlanId.entries - BUILT_INS.keys
        require(missing.isEmpty()) {
            "Missing built-in training plan: $missing"
        }
    }
}