package com.pl.myworkoutapp.domain.model.exercise

data class CustomExercise(
    override val id: ExerciseId.Custom,
    val name: String,
    val description: String?,
    // imageUri w domenie jest OK w tym wypadku, bo nie jest typem UI, tylko reprezentacją zasobu
    // (może być plik, content:// lub URL). To typowy kompromis w KMP
    val imageUri: String?,// np. "file://...", "content://...", lub URL do zasobu
    //bazowe ćwiczenie na którym jest wzorowane te ćwiczenie
    //w takim przypadku na UI będzie można używać tych samych tłumaczeń/zasobów które ma ćwiczenie bazowe
    val basedOn: ExerciseId.BuiltIn?,

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