package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pl.myworkoutapp.domain.model.Category

@Entity
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val category: Category,
    //val durationSeconds: Int,
    val met: Double
)
