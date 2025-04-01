package com.none.flow.presentation.screens.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun OnboardingPageIndicator(
    pageState: PagerState,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 10.dp,
    indicatorSpacing: Dp = 6.dp,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val currentPage by remember { derivedStateOf { pageState.currentPage } }

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageState.pageCount) {  pageIndex ->
            val isActive = currentPage == pageIndex

            val indicatordColor by animateColorAsState(
                targetValue = if (isActive) activeColor else inactiveColor,
                animationSpec = tween(),
                label = COLOR_ANIMATION_LABEL
            )

            Box(
                modifier = Modifier
                    .padding(indicatorSpacing)
                    .clip(CircleShape)
                    .background(indicatordColor)
                    .size(indicatorSize)
            )
        }
    }
}

private const val COLOR_ANIMATION_LABEL = "indicatorColorAnimation"
private const val SIZE_ANIMATION_LABEL = "indicatorSizeAnimation"
