package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.QuantityType

fun quantityChange(type: QuantityType, currentQuantityValua : Int, increase : Boolean) : Int = when(type) {
        QuantityType.REPS -> changeReps(currentQuantityValua, increase)
        QuantityType.REPS_PER_SIDE -> changeReps(currentQuantityValua, increase)
        QuantityType.DURATION -> changeDuration(currentQuantityValua, increase)
        QuantityType.DISTANCE -> changeDistance(currentQuantityValua, increase)
    }


fun changeReps(value: Int, increase: Boolean): Int {
    val adjusted = if (increase) value else value - 1
    val delta = when {
        adjusted < 10 -> 1
        adjusted < 20 -> 2
        adjusted < 50 -> 5
        else -> 10
    }
    val newValue = if (increase) value + delta else value - delta
    return newValue.coerceAtLeast(1)
}

fun changeDuration(value: Int, increase: Boolean): Int {
    val adjusted = if (increase) value else value - 1
    val delta = when {
        adjusted < 10 -> 1
        adjusted < 20 -> 2
        adjusted < 60 -> 5
        adjusted < 2 * 60 -> 10
        adjusted < 5 * 60 -> 15
        else -> 30
    }
    val newValue = if (increase) value + delta else value - delta
    return newValue.coerceAtLeast(1)
}

fun changeDistance(value: Int, increase: Boolean): Int {
    val adjusted = if (increase) value else value - 1
    val delta = when {
        adjusted < 1000 -> 100
        adjusted < 2000 -> 200
        adjusted < 5000 -> 500
        else -> 1000
    }
    val newValue = if (increase) value + delta else value - delta
    return newValue.coerceAtLeast(100)
}

