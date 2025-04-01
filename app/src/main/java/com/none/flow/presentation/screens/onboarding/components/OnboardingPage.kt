package com.none.flow.presentation.screens.onboarding.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.none.flow.utils.OnboardingPage

@Composable
@Preview(showBackground = true)
fun PreviewOnboardingPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        OnboardingPage(page = OnboardingPage.HabitFormation)
    }
}

@Composable
internal fun OnboardingPage(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OnboardingImage(imageResId = page.imageResId)
        Spacer(modifier = Modifier.height(36.dp))

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OnboardingTitle(titleResId = page.titleResId)
            OnboardingDescription(descriptionResId = page.descriptionResId)
        }
    }
}

@Composable
private fun OnboardingImage(
    @DrawableRes imageResId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth(.85f)
            .fillMaxHeight(.75f)
    )
}

@Composable
private fun OnboardingTitle(@StringRes titleResId: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = titleResId),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun OnboardingDescription(@StringRes descriptionResId: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = descriptionResId),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
