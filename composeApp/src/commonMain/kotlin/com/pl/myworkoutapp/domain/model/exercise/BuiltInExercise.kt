package com.pl.myworkoutapp.domain.model.exercise

data class BuiltInExercise(
    override val id: ExerciseId.BuiltIn,
    override val muscle: MuscleGroup,
    override val exerciseType: ExerciseType,
    override val equipment: Equipment,
    override val met: Double,
    override val quantityType: QuantityType,
    override val defaultQuantityValue: Int,
    override val secondsPerRep: Double? = null,
    override val metersPerSecond: Double? = null,
) : Exercise {
    init {
        when (quantityType) {
            QuantityType.REPS,
            QuantityType.REPS_PER_SIDE ->
                require(secondsPerRep != null)

            QuantityType.DISTANCE ->
                require(metersPerSecond != null)

            QuantityType.DURATION -> Unit
        }
    }
}