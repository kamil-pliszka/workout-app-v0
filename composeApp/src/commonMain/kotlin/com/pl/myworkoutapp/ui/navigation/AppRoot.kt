package com.pl.myworkoutapp.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.getPlatform
import com.pl.myworkoutapp.ui.common.MessageConsumer
import com.pl.myworkoutapp.ui.common.MessageCoordinator
import org.koin.compose.koinInject


@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackBarState = remember { SnackbarHostState() }
    //val currentRoute = currentRoute(navController)

    //val isInWorkout = currentRoute?.startsWith(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
    //val isInWorkout = currentRoute?.contains(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
//    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
//    val isInWorkout = currentDestination?.hierarchy?.any {
//        it.route?.startsWith(WORKOUT_EXECUTION_ROUTE_PREFIX) == true
//    } == true

    val isMobile = getPlatform().isMobile()
    val size = LocalWindowInfo.current.containerSize
    //nawigacja boczna jest przeznaczona tylko na wersje mobilne
    //w wersji desktop nie występuje RailNavigation
    val useRailNavigation = isMobile && size.width > size.height
    //val showBottomBar = !isInWorkout && !useRailNavigation
    val appStateHolder = koinInject<AppStateHolder>()
    val appState = appStateHolder.state.collectAsStateWithLifecycle()
    val showBottomBar = appState.value.showNavigationBar && !useRailNavigation
    val messageCoordinator = koinInject<MessageCoordinator>()

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
        Box(Modifier.fillMaxSize()) {
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
                        )
                    }
                    if (appState.value.showNavigationBar) {
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
                )
            }
            MessageConsumer(
                //modifier celowy aby umiejscowić wiadomości nad bottom bar
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                messages = messageCoordinator.messages
            )
        }
    }
}
