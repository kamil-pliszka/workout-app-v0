package com.pl.myworkoutapp.data.database

import androidx.room.*


@Entity(
    indices = [
        Index("basedOn"),
        Index("planId"),
    ]
)
data class CustomWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val planId: Long? = null,//wskazanie że workout jest częścią planu X dniowego,
    // tutaj trzeba będzie zmienić na Custom/BuiltIn
    val name: String?,
    val description: String?,
    val imageUri: String?,// np. "file://...", "content://...", lub URL do zasobu
    val basedOn: String?, //WorkoutId.BuiltIn?,
    val difficulty: String, //Difficulty,
    val updatedAt: Long = 0,
    //val items: List<WorkoutItem>,
    val estimatedDuration: Int?,//in seconds
    val baseKcalPerKg: Double?,
    val estimatedKcal: Int?,
)
