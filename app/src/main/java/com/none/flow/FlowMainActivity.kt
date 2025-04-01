package com.none.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.none.flow.presentation.FlowApp
import com.none.flow.presentation.rememberFlowAppState
import com.none.flow.presentation.screens.dashboard.navigation.DashboardRoute
import com.none.flow.presentation.screens.onboarding.navigation.OnboardingRoute
import com.none.flow.presentation.theme.FlowTheme
import com.none.flow.utils.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class FlowMainActivity : ComponentActivity() {

    private val viewModel by viewModels<FlowMainViewModel>()

    override fun onStart() {
        super.onStart()
        viewModel.determineStartDestination()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        val appStartDestination by viewModel.startDestination

        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                dynamicTheming = MainActivityUiState.Loading.isDynamicThemingEnabled,
            ),
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(viewModel.uiState, isSystemInDarkTheme()) { uiState, systemDark ->
                    ThemeSettings(
                        darkTheme = uiState.isDarkThemeEnabled(systemDark),
                        dynamicTheming = uiState.isDynamicThemingEnabled,
                    )
                }
                    .onEach { themeSettings = it }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = android.graphics.Color.TRANSPARENT,
                                darkScrim = android.graphics.Color.TRANSPARENT
                            ) { darkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = android.graphics.Color.TRANSPARENT,
                                darkScrim = android.graphics.Color.TRANSPARENT
                            ) { darkTheme }
                        )
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.shouldDisplaySplashScreen() || appStartDestination == null
        }

        setContent {

            if (appStartDestination == null) return@setContent

            val snackbarHostState = remember { SnackbarHostState() }
            val appState = rememberFlowAppState(snackbarHostState = snackbarHostState)

            FlowTheme(
                darkTheme = themeSettings.darkTheme,
                dynamicColor = themeSettings.dynamicTheming
            ) {
                    FlowApp(appState = appState, appStartDestination = appStartDestination!!)
            }
        }
    }
}

data class ThemeSettings(
    val darkTheme: Boolean,
    val dynamicTheming: Boolean,
)