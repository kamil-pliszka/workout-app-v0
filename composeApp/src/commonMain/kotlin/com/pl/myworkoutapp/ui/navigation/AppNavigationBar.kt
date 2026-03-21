package com.pl.myworkoutapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        NAV_ITEMS.forEach { navItem ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == navItem.route
            } == true
            NavigationBarItem(
                selected = selected, //currentRoute(navController) == navItem.route,
                onClick = {
//                    navController.navigate(navItem.route) {
//                        popUpTo(ScreenRoutes.Plans.route)
//                        launchSingleTop = true
//                    }
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.route
                    )
                },
                label = { Text(navItem.nameResource.asString()) }
            )
        }
    }
}

@Composable
fun AppNavigationRail(navController: NavController) {
    NavigationRail(
        //modifier = Modifier.fillMaxHeight(),
        //containerColor = Color.DarkGray,
        //containerColor = MaterialTheme.colorScheme.surface,
        windowInsets = WindowInsets(0, 0, 0, 0),
        //tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            //horizontalAlignment = CenterHorizontally
        ) {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            NAV_ITEMS.forEach { navItem ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == navItem.route
                } == true
                NavigationRailItem(
                    selected = selected, //currentRoute(navController) == navItem.route,
                    onClick = {
//                        navController.navigate(navItem.route) {
//                            popUpTo(ScreenRoutes.Plans.route)
//                            launchSingleTop = true
//                        }
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.route
                        )
                    },
                    label = { Text(navItem.nameResource.asString()) }
                )
            }
        }
    }

}