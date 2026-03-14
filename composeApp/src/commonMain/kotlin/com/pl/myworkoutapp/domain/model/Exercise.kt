package com.pl.myworkoutapp.domain.model

data class Exercise(
    val id: Long,
    val name: String,
    val category: Category,
    val difficulty: Difficulty,
    val durationSeconds: Int,
    val met: Double
)
