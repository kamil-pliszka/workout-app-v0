package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import kotlin.math.roundToInt

data class WorkoutBaseMetrics(
    val durationSeconds: Int,
    val baseKcalPerKg: Double
)

private data class ItemMetrics(
    val durationSeconds: Double,
    val baseKcalPerKg: Double
)

private fun List<ItemMetrics>.sum(): ItemMetrics = this.fold(0.0 to 0.0) { (dur, kcal), item ->
    (dur + item.durationSeconds) to (kcal + item.baseKcalPerKg)
}.let { (dur, kcal) -> ItemMetrics(dur, kcal) }

class EstimateWorkoutMetricsUseCase {
    fun execute(
        items: List<WorkoutItem>,
        exercises: Set<Exercise>
    ): WorkoutBaseMetrics {
        val exercisesMap: Map<ExerciseId, Exercise> = exercises.associateBy { it.id }

        val itemsSum = items.map { item ->
            estimateItem(item, exercisesMap)
        }.sum()

        return WorkoutBaseMetrics(
            durationSeconds = itemsSum.durationSeconds.roundToInt(),
            baseKcalPerKg = itemsSum.baseKcalPerKg
        )
    }

    private fun estimateItem(
        item: WorkoutItem,
        exercisesMap: Map<ExerciseId, Exercise>
    ): ItemMetrics {
        return when (item) {
            is Circuit -> {
                estimateCircuit(item, exercisesMap)
            }

            is WorkoutExercise -> {
                estimateExercise(item.quantity.value, exercisesMap.getValue(item.exerciseId))
            }
        }
    }

    private fun estimateExercise(quantity: Int, exercise: Exercise): ItemMetrics {
        val duration = estimateExerciseDuration(quantity, exercise)
        val kcal = estimateKcalPerKg(duration.toDouble(), exercise.met)

        return ItemMetrics(duration.toDouble(), kcal)
    }

    private fun estimateExerciseDuration(quantity: Int, exercise: Exercise): Float {
        return when (exercise.quantityType) {
            QuantityType.REPS -> {
                val secondsPerRep = requireNotNull(exercise.secondsPerRep)
                (quantity * secondsPerRep).toFloat()
            }

            QuantityType.REPS_PER_SIDE -> {
                val secondsPerRep = requireNotNull(exercise.secondsPerRep)
                (quantity * 2 * secondsPerRep).toFloat()
            }

            QuantityType.DURATION -> quantity.toFloat()

            QuantityType.DISTANCE -> {
                val metersPerSecond = requireNotNull(exercise.metersPerSecond)
                (quantity / metersPerSecond).toFloat()
            }
        }
    }

    private fun estimateCircuit(
        circuit: Circuit,
        exercisesMap: Map<ExerciseId, Exercise>
    ): ItemMetrics {
        val itemsSum = circuit.items.map { item ->
            estimateItem(item, exercisesMap)
        }.sum()
        return when (circuit.structure) {
            is CircuitStructure.AMRAP -> {
                val duration = circuit.structure.durationSec
                val kcal = if (itemsSum.durationSeconds == 0.0) 0.0
                else (itemsSum.baseKcalPerKg * (duration / itemsSum.durationSeconds))
                ItemMetrics(duration.toDouble(), kcal)
            }

            is CircuitStructure.EMOM -> {
                val total = circuit.structure.minutes * 60.0
                val kcal = if (itemsSum.durationSeconds == 0.0) 0.0
                else (itemsSum.baseKcalPerKg * (total / itemsSum.durationSeconds))
                ItemMetrics(total, kcal)
            }

            is CircuitStructure.Standard -> {
                ItemMetrics(
                    itemsSum.durationSeconds * circuit.structure.rounds,
                    itemsSum.baseKcalPerKg * circuit.structure.rounds
                )
            }

            is CircuitStructure.Tabata -> {
                val totalTime = (circuit.structure.workSec + circuit.structure.restSec) *
                        circuit.structure.rounds

                val kcal = estimateKcalPerKg(totalTime.toDouble(), 10.0) // tabata zwykle intensywna
                ItemMetrics(totalTime.toDouble(), kcal)
            }
        }
    }

    //kcal = MET × masa_kg × czas_h
    private fun estimateKcalPerKg(durationSeconds: Double, met: Double): Double {
        val hours = durationSeconds / 3600.0
        return (met * hours)
    }
}