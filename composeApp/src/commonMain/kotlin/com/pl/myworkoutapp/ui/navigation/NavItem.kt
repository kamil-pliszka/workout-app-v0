package com.pl.myworkoutapp.ui.navigation

import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_calendar_month
import myworkoutapplication.composeapp.generated.resources.ic_exercise
import myworkoutapplication.composeapp.generated.resources.ic_monitoring
import myworkoutapplication.composeapp.generated.resources.ic_settings_account_box
import myworkoutapplication.composeapp.generated.resources.navigation_plans
import myworkoutapplication.composeapp.generated.resources.navigation_reports
import myworkoutapplication.composeapp.generated.resources.navigation_settings
import myworkoutapplication.composeapp.generated.resources.navigation_workouts
import org.jetbrains.compose.resources.DrawableResource

data class NavItem(
    val nameResource: UiText,
    val route: String,
    val icon: DrawableResource,
)

val NAV_ITEMS = listOf(
    NavItem(
        nameResource = Res.string.navigation_plans.asUiText(),
        route = ScreenRoutes.Plans.route,
        icon = Res.drawable.ic_calendar_month,
    ),

    NavItem(
        nameResource = Res.string.navigation_workouts.asUiText(),
        route = ScreenRoutes.WorkoutsRoot.route,
        icon = Res.drawable.ic_exercise,
    ),

    NavItem(
        nameResource = Res.string.navigation_reports.asUiText(),
        route = ScreenRoutes.Reports.route,
        icon = Res.drawable.ic_monitoring,
    ),

    NavItem(
        nameResource = Res.string.navigation_settings.asUiText(),
        route = ScreenRoutes.Settings.route,
        icon = Res.drawable.ic_settings_account_box,
    ),

)