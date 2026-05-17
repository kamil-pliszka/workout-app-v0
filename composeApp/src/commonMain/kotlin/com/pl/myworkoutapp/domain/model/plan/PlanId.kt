package com.pl.myworkoutapp.domain.model.plan

import kotlin.jvm.JvmInline

sealed interface PlanId {
    @JvmInline
    value class BuiltIn(val id: BuiltInTrainingPlanId) : PlanId

    @JvmInline
    value class Custom(val id: Long) : PlanId {
        companion object {
            val NEW = Custom(0)
        }

        fun isNew() = this == NEW
    }
}

fun Long.asPlanId(): PlanId.Custom = PlanId.Custom(this)

fun BuiltInTrainingPlanId.asPlanId(): PlanId.BuiltIn = PlanId.BuiltIn(this)

fun PlanId.Custom.toLong() = this.id

fun PlanId.BuiltIn.toBuiltInPlanId() = this.id


private const val BUILTIN_PREFIX = "BuiltInPlan:"
private const val CUSTOM_PREFIX = "CustomPlan:"

fun String.toPlanIdOrNull(): PlanId? = when {
    startsWith(BUILTIN_PREFIX) -> {
        val name = removePrefix(BUILTIN_PREFIX)
        BuiltInTrainingPlanId.entries.find { it.name == name }?.asPlanId()
    }

    startsWith(CUSTOM_PREFIX) -> {
        removePrefix(CUSTOM_PREFIX).toLongOrNull()?.asPlanId()
    }

    else -> null
}

fun PlanId.asString() = when (this) {
    is PlanId.BuiltIn -> BUILTIN_PREFIX + this.id.name
    is PlanId.Custom -> CUSTOM_PREFIX + this.id
}
