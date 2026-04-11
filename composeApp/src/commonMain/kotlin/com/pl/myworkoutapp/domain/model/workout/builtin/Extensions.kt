package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise

//fun BuiltInExerciseId.toExercise(): BuiltInExercise = BuiltInExerciseRegistry.get(this)
//fun BuiltInExercise.with(quantity: Quantity) = WorkoutExercise(this, quantity)

//można jak powyżej, ale to wydłuża kod do 2 linii, a wole zmieścić się w jednej
fun BuiltInExerciseId.with(quantity: Quantity) : WorkoutExercise {
    val builtInExe: BuiltInExercise = BuiltInExerciseRegistry.get(this) // ?: error("Exercise not found in regstry: $this"),
    require(quantity.type == builtInExe.quantityType) {
        "Exercise: ${builtInExe.id} requires quantityType: ${builtInExe.quantityType}, got: ${quantity.type}"
    }
    return WorkoutExercise(
        builtInExe.id,
        quantity
    )
}

fun BuiltInExerciseId.withDuration(sec: Int) = this.with(
    Quantity(
        QuantityType.DURATION,
        sec
    )
)
fun BuiltInExerciseId.withReps(reps: Int) = this.with(
    Quantity(
        QuantityType.REPS,
        reps
    )
)
fun BuiltInExerciseId.withRepsPerSide(rps: Int) = this.with(
    Quantity(
        QuantityType.REPS_PER_SIDE,
        rps
    )
)


