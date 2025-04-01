package com.none.flow.presentation.screens.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(route = SettingsRoute, navOptions)
}

fun NavGraphBuilder.addSettingsScreen(
    onCloseScreen: () -> Unit
) {
    composable<SettingsRoute> {
        SettingsScreen(onCloseScreen = onCloseScreen)
    }
}