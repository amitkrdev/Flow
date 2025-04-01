package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.none.flow.R

@Composable
internal fun HabitIconPicker(
    selectedIconEmoji: String?,
    onSelectIcon: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconPicker(selectedIconEmoji, onSelectIcon, modifier)
}

@Composable
private fun IconPicker(
    selectedIconEmoji: String?,
    onSelectIcon: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconPreviewCard(selectedIconEmoji, onSelectIcon)
        Text(stringResource(R.string.action_pick_icon))
    }
}

@Composable
private fun IconPreviewCard(
    selectedIconEmoji: String?,
    onSelectIcon: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = CircleShape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = onSelectIcon,
        modifier = modifier
    ) {
        Box(modifier = Modifier.requiredSize(96.dp), contentAlignment = Alignment.Center) {
            if (selectedIconEmoji != null) {
                Text(text = selectedIconEmoji, style = MaterialTheme.typography.headlineLarge)
            }else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_reaction),
                    contentDescription = stringResource(R.string.action_pick_icon),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
