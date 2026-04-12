package com.pl.myworkoutapp.data.database

import androidx.room.*


@Entity(
    indices = [
        Index("basedOn"),
    ]
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String?,
    val description: String?,
    val imageUri: String?,// np. "file://...", "content://...", lub URL do zasobu
    val basedOn: String?, //WorkoutId.BuiltIn?,
    val difficulty: String, //Difficulty,
    val updatedAt: Long = 0,
    //val items: List<WorkoutItem>,
)
