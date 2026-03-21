package com.pl.myworkoutapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.navigation_plans
import myworkoutapplication.composeapp.generated.resources.navigation_reports
import myworkoutapplication.composeapp.generated.resources.navigation_settings
import myworkoutapplication.composeapp.generated.resources.navigation_workouts

data class NavItem(
    //name resource
    val nameResource: UiText,
    val route: String,
    val icon: ImageVector,
)

val NAV_ITEMS = listOf(
    NavItem(
        nameResource = Res.string.navigation_plans.asUiText(),
        route = ScreenRoutes.Plans.route,
        icon = Icons.Default.DateRange,
    ),

    NavItem(
        nameResource = Res.string.navigation_workouts.asUiText(),
        route = ScreenRoutes.Workouts.route,
        icon = Icons.Default.Home,
    ),

    NavItem(
        nameResource = Res.string.navigation_reports.asUiText(),
        route = ScreenRoutes.Reports.route,
        icon = Icons.AutoMirrored.Filled.List,
    ),

    NavItem(
        nameResource = Res.string.navigation_settings.asUiText(),
        route = ScreenRoutes.Settings.route,
        icon = Icons.Default.Settings,
    ),

)