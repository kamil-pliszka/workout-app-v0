package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pl.myworkoutapp.domain.model.Difficulty

//TODO - encje potrzebne w przypadku umożliwienia manualnego tworzenia/edycji planów treningowych
@Entity
data class CustomWorkoutPlanEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    //val category: Category,
    val difficulty: Difficulty,
    val exerciseIds: List<Int>
)
