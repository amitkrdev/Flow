package com.none.flow.presentation.screens.reorder.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.reorder.ReorderHabitsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ReorderHabitsRoute

fun NavController.navigateToReorderHabits(navOptions: NavOptions? = null) {
    navigate(route = ReorderHabitsRoute, navOptions)
}

fun NavGraphBuilder.addReorderHabitsScreen(
    onCloseScreen: () -> Unit,
) {
    composable<ReorderHabitsRoute> {
        ReorderHabitsScreen(onCloseScreen = onCloseScreen)
    }
}
