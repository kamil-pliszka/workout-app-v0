package com.pl.myworkoutapp.ui.workouts

data class WorkoutsUiState(
    val isLoading: Boolean = true,
    val workouts: List<WorkoutUiModel> = emptyList(),
    val currentPage: Int = 0
)
