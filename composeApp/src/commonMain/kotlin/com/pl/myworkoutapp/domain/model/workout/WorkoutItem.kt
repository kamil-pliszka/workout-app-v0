package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Quantity

sealed interface WorkoutItem

data class WorkoutExercise(
    // chcę całego dostępu do ćwiczenia, żeby zwalidować quantity,
    // zaś na poziomie bazy będzie zapisywane ExerciseId
    //val exercise: Exercise,
    val exerciseId: ExerciseId,
    val quantity: Quantity
) : WorkoutItem {
    init {
        /*
        require(quantity.type == exercise.quantityType) {
            "Exercise: ${exercise.id} requires quantityType: ${exercise.quantityType}, got: ${quantity.type}"
        }*/
        require(quantity.value > 0) {
            "quantity must be > 0"
        }
    }
}

enum class Phase {
    WARMUP, MAIN, COOLDOWN
}

sealed interface CircuitStructure {
    data class Standard(
        // ilość cykli/obwodów
        val rounds: Int
    ) : CircuitStructure //ITEMS - dowolne: ćwiczenie lub obwód

    data class EMOM(//Every Minute On Minute
        // The goal of an EMOM workout is to complete a certain number of reps of a
        // particular exercise within 60 seconds and to use whatever time is left in that minute
        // to rest before moving on to the next set
        val minutes: Int
    ) : CircuitStructure //ITEMS - dokładnie 1 ćwiczenie na powtórzenie REPS/REPS_PER_SIDE, a na czas - nie wiadomo

    data class AMRAP(//As Many Reps/Rounds As Possible
        // The goal is to do as many reps of one specific exercise — or as many rounds of a circuit
        // — in a designated amount of time
        val durationSec: Int
    ) : CircuitStructure //ITEMS - dowlne ćwiczenie/ćwiczenia

    data class Tabata(
        val rounds: Int, //liczba cykli
        // trening interwałowy (HIIT), trwający zazwyczaj 4 minuty. Polega na wykonywaniu ćwiczeń
        // przez 20 sekund w maksymalnym tempie, po których następuje 10 sekund przerwy,
        // powtarzając cykl 8 razy
        val workSec: Int,
        val restSec: Int,
    ) : CircuitStructure //ITEMS - dowolne ćwiczenie/ćwiczenia
}

//zbiór ćwiczeń w ramach tzw SET/Obwód
data class Circuit(
    val phase: Phase,
    val name: String? = null,              // np "warm-up", "cool-down", "Core finisher", "AMRAP 10 min"
    val structure: CircuitStructure, // = CircuitStructure.Standard(1),
    val items: List<WorkoutItem>,
) : WorkoutItem {
    init {
        require(items.isNotEmpty()) {
            "items must be not empty"
        }
    }
}