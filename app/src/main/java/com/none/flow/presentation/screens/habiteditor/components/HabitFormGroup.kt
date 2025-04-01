package com.none.flow.presentation.screens.habiteditor.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun HabitFormGroup(
    @StringRes sectionTitleResId: Int?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
//    Card(
//        shape = MaterialTheme.shapes.large,
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainer,
//            contentColor = MaterialTheme.colorScheme.onSurface
//        ),
//        modifier = modifier.fillMaxWidth()
//    ) {
//    }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sectionTitleResId?.let {
            HabitFormGroupHeader(titleResId = it)
        } ?: Spacer(modifier = Modifier.height(4.dp))

        content.invoke(this)
    }
}

@Composable
private fun HabitFormGroupHeader(
    @StringRes titleResId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 12.dp, bottom = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
