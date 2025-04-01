package com.none.flow.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.none.flow.presentation.navigation.FlowNavHost
import com.none.flow.utils.SnackbarManager
import kotlin.reflect.KClass

@Composable
fun FlowApp(
    appState: FlowAppState,
    appStartDestination: KClass<*>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
            )
        },
        contentWindowInsets = WindowInsets.navigationBars.also { WindowInsets.statusBars },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            FlowNavHost(
                appState = appState,
                appStartDestination = appStartDestination
            )
        }
    }
}
