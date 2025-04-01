package com.none.flow.presentation.screens.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.dashboard.DashboardScreen
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

fun NavController.navigateToDashboard(navOptions: NavOptions? = null) {
    navigate(route = DashboardRoute, navOptions)
}

fun NavGraphBuilder.addDashboardScreen(
    onEditHabit: (String?) -> Unit,
    onViewStatistics: (String?) -> Unit,
    onOpenReorderHabits: () -> Unit,
    onOpenArchived: () -> Unit,
    onOpenSettings: () -> Unit
) {
    composable<DashboardRoute> {
        DashboardScreen(
            onEditHabit = onEditHabit,
            onOpenSettings = onOpenSettings,
            onOpenArchived = onOpenArchived,
            onOpenReorderHabits = onOpenReorderHabits,
            onViewStatistics = onViewStatistics
        )
    }
}