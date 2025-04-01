package com.none.flow.presentation.screens.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.none.flow.R
import com.none.flow.presentation.screens.onboarding.components.OnboardingPage
import com.none.flow.presentation.screens.onboarding.components.OnboardingPageIndicator
import com.none.flow.utils.OnboardingPage
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    OnboardingScreen(
        uiState = OnboardingUiState.Incomplete,
        onGetStarted = {},
        onOnboardingCompleted = {}
    )
}


@Composable
internal fun OnboardingScreen(
    onOnboardingCompleted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
    OnboardingScreen(
        uiState = onboardingUiState,
        onOnboardingCompleted = onOnboardingCompleted,
        onGetStarted = viewModel::markOnboardingAsComplete
    )
}

@Composable
private fun OnboardingScreen(
    uiState: OnboardingUiState,
    onOnboardingCompleted: () -> Unit,
    onGetStarted: () -> Unit
) {
    val onboardingPages = remember {
        listOf(
            OnboardingPage.HabitFormation,
            OnboardingPage.Consistency,
            OnboardingPage.HabitProgress
        )
    }

    val pageState = rememberPagerState { onboardingPages.size }

    val coroutineScope = rememberCoroutineScope()
    val isFinalStep = pageState.currentPage == pageState.pageCount - 1

    LaunchedEffect(uiState) {
        if (uiState is OnboardingUiState.Complete) onOnboardingCompleted()
    }

    Scaffold(
        topBar = { OnboardingTopBar() },
        bottomBar = {
            OnboardingBottomBar(
                isFinalStep = isFinalStep,
                onActionClick = {
                    if (isFinalStep) {
                        onGetStarted()
                    } else {
                        coroutineScope.launch { pageState.animateScrollToPage(pageState.currentPage + 1) }
                    }
                })
        }
    ) { contentPadding ->
        OnboardingContent(
            pages = onboardingPages,
            pagerState = pageState,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.welcome_msg)) }
    )
}

@Composable
private fun OnboardingBottomBar(
    isFinalStep: Boolean,
    onActionClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        OnboardingActionButton(isFinalStep = isFinalStep, onClick = onActionClick)
    }
}

@Composable
private fun OnboardingContent(
    pagerState: PagerState,
    pages: List<OnboardingPage>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        OnboardingPager(
            pagerState = pagerState,
            pages = pages,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OnboardingPageIndicator(pageState = pagerState)
    }
}

@Composable
private fun OnboardingPager(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { pageIndex ->
        OnboardingPage(page = pages[pageIndex])
    }
}

@Composable
private fun OnboardingActionButton(isFinalStep: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                id = if (isFinalStep) R.string.action_start
                else R.string.action_next
            )
        )
    }
}