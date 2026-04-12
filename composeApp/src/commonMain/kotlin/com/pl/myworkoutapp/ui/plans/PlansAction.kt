package com.pl.myworkoutapp.ui.plans

sealed interface PlansAction {
    data class OnStartPlan(val planId: String) : PlansAction
    data class OnPageChanged(val index: Int) : PlansAction

}