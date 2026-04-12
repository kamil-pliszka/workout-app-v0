package com.pl.myworkoutapp.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.pl.myworkoutapp.domain.model.exercise.asString
import com.pl.myworkoutapp.domain.model.workout.asString
import com.pl.myworkoutapp.ui.common.ObserveAsEvents
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionScreen
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionViewModel
import com.pl.myworkoutapp.ui.exercises.ExerciseEditorDialog
import com.pl.myworkoutapp.ui.exercises.ExerciseEditorViewModel
import com.pl.myworkoutapp.ui.plans.PlansScreen
import com.pl.myworkoutapp.ui.plans.PlansViewModel
import com.pl.myworkoutapp.ui.reports.ReportsScreen
import com.pl.myworkoutapp.ui.reports.ReportsViewModel
import com.pl.myworkoutapp.ui.settings.SettingsScreen
import com.pl.myworkoutapp.ui.settings.SettingsViewModel
import com.pl.myworkoutapp.ui.workouts.*
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appNavigator : AppNavigator = koinInject<AppNavigator>(),
) {
    ObserveAsEvents(appNavigator.navEvents) { event ->
        when (event) {
            AppNavigatorEvent.PopBackStack -> navController.popBackStack()
            is AppNavigatorEvent.NavToExerciseEditor -> {
                println("nav to: ${event.exerciseId.asString()}")
                navController.navigate(ScreenRoutes.ExerciseEditor.create(event.exerciseId.asString()))
            }
            is AppNavigatorEvent.NavToWorkoutDetails -> {
                println("nav to workout details: ${event.workoutId.asString()}")
                navController.navigate(ScreenRoutes.WorkoutDetails.create(event.workoutId.asString()))
            }
            is AppNavigatorEvent.NavToWorkoutExecution -> {
                println("nav to workout exec: ${event.workoutId.asString()}")
                navController.navigate(ScreenRoutes.WorkoutExecution.create(event.workoutId.asString()))
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.WorkoutsRoot.route,
        modifier = modifier
    ) {

        // MAIN GRAPH
        composable(ScreenRoutes.Plans.route) {
            val viewModel: PlansViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            PlansScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }

        navigation(
            startDestination = ScreenRoutes.WorkoutsList.route,
            route = ScreenRoutes.WorkoutsRoot.route
        ) {
            composable(ScreenRoutes.WorkoutsList.route) {
                val viewModel: WorkoutsViewModel = koinViewModel()
                //UiEventConsumer(snackbarHostState, viewModel.events)
                val state by viewModel.state.collectAsStateWithLifecycle()
                WorkoutsScreen(
                    state = state,
                    onAction = viewModel::onAction
                )
            }
            composable(ScreenRoutes.WorkoutDetails.route) { backStackEntry ->
                val viewModel: WorkoutDetailsViewModel =
                    koinViewModel(viewModelStoreOwner = backStackEntry)
                //UiEventConsumer(snackbarHostState, viewModel.events)
                val state by viewModel.state.collectAsStateWithLifecycle()
                WorkoutDetailsScreen(
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
        composable(ScreenRoutes.Reports.route) {
            val viewModel: ReportsViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReportsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable(ScreenRoutes.Settings.route) {
            //val viewModel: SettingsViewModel = sharedKoinViewModel(navController, "settings_graph")
            val viewModel: SettingsViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            SettingsScreen(
                state = state,
                onAction = viewModel::onAction,
            )
        }

        dialog(
            route = ScreenRoutes.ExerciseEditor.route,
        ) { backStackEntry ->
            val viewModel: ExerciseEditorViewModel =
                koinViewModel(viewModelStoreOwner = backStackEntry)
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            ExerciseEditorDialog(
                state = state,
                onAction = viewModel::onAction
            )
        }

        // EXECUTION GRAPH
        composable(
            ScreenRoutes.WorkoutExecution.route
        ) { backStackEntry ->
            val viewModel: WorkoutExecutionViewModel = koinViewModel(
                viewModelStoreOwner = backStackEntry
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            WorkoutExecutionScreen(
                state = state,
                onFinish = { navController.popBackStack() }
            )
        }
    }

}


@Composable
private fun isLandscape(): Boolean {
    val size = LocalWindowInfo.current.containerSize
    return size.width > size.height
}

/*
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController,
    graphRoute: String
): T {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(graphRoute)
    }

    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}*/

@Composable
inline fun <reified T : ViewModel> sharedKoinViewModel(
    navController: NavController,
    graphRoute: String
): T {
    val parentEntry = remember {
        navController.getBackStackEntry(graphRoute)
    }

    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}