package com.pl.myworkoutapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pl.myworkoutapp.domain.model.Category
import com.pl.myworkoutapp.domain.model.Difficulty

@Entity
data class WorkoutPlanEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val category: Category,
    val difficulty: Difficulty,
    val exerciseIds: List<Int>
)
