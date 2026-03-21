package com.pl.myworkoutapp.ui.plans

data class PlansUiState(
    val isLoading: Boolean = true,
    val plans: List<PlanUiModel> = emptyList(),
    val currentPage: Int = 0

)
