package com.pl.myworkoutapp.ui.navigation

const val WORKOUT_EXECUTION_ROUTE_PREFIX = "workout_execution"
sealed class ScreenRoutes(val route: String) {
    // com.pl.myworkoutapp.androidapp.main
    data object Plans : ScreenRoutes("plans")
    data object Workouts : ScreenRoutes("workouts")
    data object Reports : ScreenRoutes("reports")
    data object Settings : ScreenRoutes("settings")

    // execution
    data object WorkoutExecution : ScreenRoutes("$WORKOUT_EXECUTION_ROUTE_PREFIX/{workoutId}") {
        fun create(workoutId: String) = "$WORKOUT_EXECUTION_ROUTE_PREFIX/$workoutId"
    }
}