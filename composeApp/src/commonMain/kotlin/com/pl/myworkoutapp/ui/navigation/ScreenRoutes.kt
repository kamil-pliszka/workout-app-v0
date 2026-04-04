package com.pl.myworkoutapp.ui.navigation

const val WORKOUT_EXECUTION_ROUTE_PREFIX = "workout_execution"
const val WORKOUT_DETAILS_ROUTE_PREFIX = "workout_details"
sealed class ScreenRoutes(val route: String) {
    // com.pl.myworkoutapp.androidapp.main
    data object Plans : ScreenRoutes("plans")

    data object WorkoutsRoot : ScreenRoutes("root_of_workouts")
    data object WorkoutsList : ScreenRoutes("workouts_list")
    data object Reports : ScreenRoutes("reports")
    data object Settings : ScreenRoutes("settings")
    //data object Camera : ScreenRoutes("camera")

    // execution
    data object WorkoutExecution : ScreenRoutes("$WORKOUT_EXECUTION_ROUTE_PREFIX/{workoutId}") {
        fun create(workoutId: String) = "$WORKOUT_EXECUTION_ROUTE_PREFIX/$workoutId"
    }
    data object WorkoutDetails : ScreenRoutes("$WORKOUT_DETAILS_ROUTE_PREFIX/{workoutId}") {
        fun create(workoutId: String) = "$WORKOUT_DETAILS_ROUTE_PREFIX/$workoutId"
    }
}