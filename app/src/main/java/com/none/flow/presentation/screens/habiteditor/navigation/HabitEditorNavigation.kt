package com.none.flow.presentation.screens.habiteditor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.habiteditor.HabitEditorScreen
import kotlinx.serialization.Serializable

@Serializable
data class HabitEditorRoute(val id: String?)

fun NavController.navigateToHabitEditor(id: String?, navOptions: NavOptions? = null) {
    navigate(route = HabitEditorRoute(id), navOptions)
}

fun NavGraphBuilder.addHabitEditorScreen(
    onHabitUpdate: () -> Unit, onCloseScreen: () -> Unit,
) {
    composable<HabitEditorRoute> {
        HabitEditorScreen(onHabitUpdate = onHabitUpdate, onCloseScreen = onCloseScreen)
    }
}