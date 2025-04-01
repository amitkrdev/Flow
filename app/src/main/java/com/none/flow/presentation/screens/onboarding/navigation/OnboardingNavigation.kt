package com.none.flow.presentation.screens.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.none.flow.presentation.screens.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable
object OnboardingRoute

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(route = OnboardingRoute, navOptions)
}

fun NavGraphBuilder.addOnboardingScreen(onOnboardingCompleted: () -> Unit) {
    composable<OnboardingRoute> {
        OnboardingScreen(onOnboardingCompleted = onOnboardingCompleted)
    }
}