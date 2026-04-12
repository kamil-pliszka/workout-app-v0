package com.pl.myworkoutapp.data.database

data class FlatWorkoutItem(
    val itemEntity : WorkoutItemEntity,
    val parentIndex : Int?,
    val position : Int, // kolejność w ramach parenta
)
