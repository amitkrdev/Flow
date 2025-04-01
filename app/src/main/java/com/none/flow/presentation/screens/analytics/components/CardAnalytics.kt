package com.none.flow.presentation.screens.analytics.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsCard(
    icon: Painter,
    heading: String,
    subheading: String,
    modifier: Modifier = Modifier,
    additionalContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(all = 20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    AnalyticsCardHeading(heading = heading)
                    Spacer(modifier = Modifier.height(2.dp))
                    AnalyticsCardSubHeading(subheading = subheading)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Icon(painter = icon, contentDescription = null)
            }

            additionalContent?.invoke(this)
        }
    }
}

@Composable
private fun AnalyticsCardHeading(
    heading: String,
    modifier: Modifier = Modifier
) {
    Text(text = heading, style = MaterialTheme.typography.labelMedium, modifier = modifier)
}


@Composable
private fun AnalyticsCardSubHeading(
    subheading: String,
    modifier: Modifier = Modifier
) {
    Text(text = subheading, style = MaterialTheme.typography.headlineMedium, modifier = modifier)
}