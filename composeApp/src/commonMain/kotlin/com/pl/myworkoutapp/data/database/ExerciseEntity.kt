package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO - encje potrzebne w przypadku umożliwienia manualnego tworzenia ćwiczeń
@Entity
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    //val category: Category,
    //val durationSeconds: Int,
    val met: Double
)
