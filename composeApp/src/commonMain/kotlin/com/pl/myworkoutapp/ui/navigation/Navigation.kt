package com.pl.myworkoutapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pl.myworkoutapp.ui.common.asString
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionScreen
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionViewModel
import com.pl.myworkoutapp.ui.plans.PlansAction
import com.pl.myworkoutapp.ui.plans.PlansScreen
import com.pl.myworkoutapp.ui.plans.PlansViewModel
import com.pl.myworkoutapp.ui.reports.ReportsScreen
import com.pl.myworkoutapp.ui.reports.ReportsViewModel
import com.pl.myworkoutapp.ui.settings.SettingsScreen
import com.pl.myworkoutapp.ui.settings.SettingsViewModel
import com.pl.myworkoutapp.ui.workouts.WorkoutsScreen
import com.pl.myworkoutapp.ui.workouts.WorkoutsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Plans.route,
        modifier = modifier
    ) {

        // MAIN GRAPH
        composable(ScreenRoutes.Plans.route) {
            val viewModel : PlansViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            PlansScreen(
                state = state,
                onAction = { action ->
                    viewModel.onAction(action)
                    if (action is PlansAction.NavToWorkout) {
                        navController.navigate(ScreenRoutes.WorkoutExecution.create(action.workoutId.asString()))
                    }
                }
            )
        }
        composable(ScreenRoutes.Workouts.route) {
            val viewModel : WorkoutsViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            WorkoutsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable(ScreenRoutes.Reports.route) {
            val viewModel : ReportsViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReportsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable(ScreenRoutes.Settings.route) {
            val viewModel : SettingsViewModel = koinViewModel()
            //UiEventConsumer(snackbarHostState, viewModel.events)
            val state by viewModel.state.collectAsStateWithLifecycle()
            SettingsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }

        // EXECUTION GRAPH
        composable(
            ScreenRoutes.WorkoutExecution.route
        ) { backStackEntry ->
            //val workoutId = backStackEntry.arguments?.getString("workoutId")!!
            val viewModel : WorkoutExecutionViewModel = koinViewModel(
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
