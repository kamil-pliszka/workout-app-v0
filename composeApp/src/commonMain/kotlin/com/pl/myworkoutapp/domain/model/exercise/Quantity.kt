package com.pl.myworkoutapp.domain.model.exercise

enum class QuantityType {
    REPS,
    REPS_PER_SIDE,
    DURATION,
    DISTANCE,
}

data class Quantity(
    val type: QuantityType,
    val value: Int
)