package com.none.flow.presentation.screens.analytics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kotlin.reflect.KFunction0

@Serializable
data class AnalyticsRoute(val habitId: String?)

fun NavController.navigateToAnalytics(habitId: String?, navOptions: NavOptions? = null) {
    navigate(route = AnalyticsRoute(habitId = habitId), navOptions)
}

fun NavGraphBuilder.addAnalyticsScreen(onCloseScreen: KFunction0<Unit>) {
    composable<AnalyticsRoute> {
    }
}
