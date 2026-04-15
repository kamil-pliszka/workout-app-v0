package com.pl.myworkoutapp.data.database

import androidx.room.*

@Entity(
    indices = [
        Index("basedOn"),
        Index("name"),
    ]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String?,
    val imageUri: String?,
    val basedOn: String?, //ExerciseId.BuiltIn?,
    val muscle: String,
    val exerciseType: String,
    val equipment: String,
    val met: Double,
    val quantityType: String,
    val updatedAt: Long = 0,
)
