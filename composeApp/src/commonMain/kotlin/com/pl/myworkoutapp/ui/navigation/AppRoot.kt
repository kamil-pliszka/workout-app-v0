package com.pl.myworkoutapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pl.myworkoutapp.getPlatform


@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackBarState = remember { SnackbarHostState() }
    //val currentRoute = currentRoute(navController)

    //val isInWorkout = currentRoute?.startsWith(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
    //val isInWorkout = currentRoute?.contains(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isInWorkout = currentDestination?.hierarchy?.any {
        it.route?.startsWith(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
    } == true

    val isMobile = getPlatform().isMobile()
    val size = LocalWindowInfo.current.containerSize
    //nawigacja boczna jest przeznaczona tylko na wersje mobilne
    //w wersji desktop nie występuje RailNavigation
    val useRailNavigation = isMobile && size.width > size.height
    val showBottomBar = !isInWorkout && !useRailNavigation

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(navController)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarState
            )
        },
    ) { innerPadding ->
        if (useRailNavigation) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                Box(Modifier.weight(1f)) {
                    Navigation(
                        navController = navController,
                        snackbarHostState = snackBarState
                    )
                }
                if (!isInWorkout) {
                    AppNavigationRail(
                        navController = navController,
                    )
                }
            }
        } else {
            Navigation(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                navController = navController,
                snackbarHostState = snackBarState
            )
        }
    }
}
