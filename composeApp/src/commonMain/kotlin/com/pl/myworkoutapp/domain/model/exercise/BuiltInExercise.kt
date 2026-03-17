package com.pl.myworkoutapp.domain.model.exercise

data class BuiltInExercise(
    override val id: ExerciseId.BuiltIn,
    override val muscle: MuscleGroup,
    override val exerciseType: ExerciseType,
    override val equipment: Equipment,
    override val met: Double,
    override val quantityType: QuantityType
) : Exercise