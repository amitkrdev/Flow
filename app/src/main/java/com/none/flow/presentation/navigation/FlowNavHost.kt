package com.none.flow.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.none.flow.presentation.FlowAppState
import com.none.flow.presentation.screens.analytics.navigation.addAnalyticsScreen
import com.none.flow.presentation.screens.archived.navigation.addArchivedScreen
import com.none.flow.presentation.screens.dashboard.navigation.addDashboardScreen
import com.none.flow.presentation.screens.habiteditor.navigation.addHabitEditorScreen
import com.none.flow.presentation.screens.onboarding.navigation.addOnboardingScreen
import com.none.flow.presentation.screens.reorder.navigation.addReorderHabitsScreen
import com.none.flow.presentation.screens.settings.navigation.addSettingsScreen
import kotlin.reflect.KClass

@Composable
fun FlowNavHost(
    appState: FlowAppState,
    appStartDestination: KClass<*>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = appStartDestination,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn()
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeOut()
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeIn()
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeOut()
        }
    ) {
        addOnboardingScreen(onOnboardingCompleted = appState::goToDashboardScreen)

        addDashboardScreen(
            onEditHabit = appState::goToHabitEditorScreen,
            onOpenArchived = appState::goToArchivedScreen,
            onOpenSettings = appState::goToSettingsScreen,
            onOpenReorderHabits = appState::goToReorderHabitsScreen,
            onViewStatistics = appState::goToAnalyticsScreen
        )

        addHabitEditorScreen(
            onCloseScreen = appState::goBack,
            onHabitUpdate = appState::goToDashboardScreen
        )

        addArchivedScreen(onCloseScreen = appState::goBack)

        addReorderHabitsScreen(onCloseScreen = appState::goBack)

        addAnalyticsScreen(onCloseScreen = appState::goBack)

        addSettingsScreen(onCloseScreen = appState::goBack)
    }
}
