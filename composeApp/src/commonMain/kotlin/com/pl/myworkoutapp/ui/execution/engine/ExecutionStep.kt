package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.Quantity

/*
TODO - docelowo do dodania kiedyś:
    data class CircuitStart(
        val name: String
    ) : ExecutionStep

    data class CircuitEnd(
        val name: String
    ) : ExecutionStep
}
*/

/**
 * Znormalizowany execution plan.
 * Odpowiada za:
 * reprezentację pojedynczego kroku runtime workflow
 *
 * Przykłady:
 * Exercise
 * Rest
 * później:
 * CircuitStart
 * CircuitEnd
 * Warmup
 * Cooldown
 *
 * To NIE jest model domenowy.
 * To runtime instruction dla engine.
 */
sealed interface ExecutionStep {

    data class ExerciseStep(
        val exercise: Exercise,
        val quantity: Quantity
    ) : ExecutionStep

    data class RestStep(
        val durationSeconds: Int,
    ) : ExecutionStep
}