package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.Quantity

sealed interface WorkoutItem

data class WorkoutExercise(
    // chcę całego dostępu do ćwiczenia, żeby zwalidować quantity,
    // zaś na poziomie bazy będzie zapisywane ExerciseId
    val exercise: Exercise,
    val quantity: Quantity
) : WorkoutItem {
    init {
        require(quantity.type == exercise.quantityType) {
            "Exercise: ${exercise.id} requires quantityType: ${exercise.quantityType}, got: ${quantity.type}"
        }
    }
}

enum class Phase {
    WARMUP, COOLDOWN, MAIN
}

sealed interface CircuitStructure {
    data object Standard : CircuitStructure

    data class EMOM(//Every Minute On Minute
        val minutes: Int
    ) : CircuitStructure

    data class AMRAP(//As Many Rounds As Possible
        val durationSec: Int
    ) : CircuitStructure

    data class Tabata(
        val workSec: Int,
        val restSec: Int,
    ) : CircuitStructure
}

//zbiór ćwiczeń w ramach tzw SET/Obwód
data class Circuit(
    //override val order: Int = 0,
    val phase: Phase,
    val name: String? = null,              // np "warm-up", "cool-down", "Core finisher", "AMRAP 10 min"
    val rounds: Int,
    val structure: CircuitStructure = CircuitStructure.Standard,
    val items: List<WorkoutItem>,
) : WorkoutItem