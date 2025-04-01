package com.none.flow.presentation.screens.archived.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.archived.ArchivedHabitsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ArchivedRoute

fun NavController.navigateToArchived(navOptions: NavOptions? = null) {
    navigate(route = ArchivedRoute, navOptions)
}

fun NavGraphBuilder.addArchivedScreen(
    onCloseScreen: () -> Unit,
) {
    composable<ArchivedRoute> {
        ArchivedHabitsScreen(onCloseScreen = onCloseScreen)
    }
}
