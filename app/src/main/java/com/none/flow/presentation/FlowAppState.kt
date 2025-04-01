package com.none.flow.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.none.flow.presentation.screens.analytics.navigation.navigateToAnalytics
import com.none.flow.presentation.screens.archived.navigation.navigateToArchived
import com.none.flow.presentation.screens.dashboard.navigation.navigateToDashboard
import com.none.flow.presentation.screens.habiteditor.navigation.navigateToHabitEditor
import com.none.flow.presentation.screens.onboarding.navigation.navigateToOnboarding
import com.none.flow.presentation.screens.reorder.navigation.navigateToReorderHabits
import com.none.flow.presentation.screens.settings.navigation.navigateToSettings
import com.none.flow.utils.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberFlowAppState(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): FlowAppState {
    return remember(navController) {
        FlowAppState(snackbarHostState, navController, snackbarManager, coroutineScope)
    }
}

@Stable
class FlowAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { (message, actionLabel, result) ->
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
                result.complete(snackbarResult == SnackbarResult.ActionPerformed)
            }
        }
    }

    val activeDestination: NavDestination?
        @Composable get() {
            return produceState<NavDestination?>(initialValue = null, key1 = navController) {
                navController.currentBackStackEntryFlow.collect { entry ->
                    value = entry.destination
                }
            }.value
        }

    fun getDefaultNavOptions(): NavOptions = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }

    fun goToOnboardingScreen() {
        navController.navigateToOnboarding(navOptions = getDefaultNavOptions())
    }

    fun goToDashboardScreen() {
        navController.navigateToDashboard(navOptions = getDefaultNavOptions())
    }

    fun goToHabitEditorScreen(habitId: String?) {
        navController.navigateToHabitEditor(id = habitId)
    }

    fun goToArchivedScreen() {
        navController.navigateToArchived()
    }

    fun goToReorderHabitsScreen() {
        navController.navigateToReorderHabits()
    }

    fun goToAnalyticsScreen(habitId: String?) {
        navController.navigateToAnalytics(habitId)
    }

    fun goToSettingsScreen() {
        navController.navigateToSettings()
    }

    fun goBack() {
        navController.navigateUp()
    }
}

