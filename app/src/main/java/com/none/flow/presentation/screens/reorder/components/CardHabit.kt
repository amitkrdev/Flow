package com.none.flow.presentation.screens.reorder.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.none.flow.data.model.Habit
import java.time.Instant

@Preview(showBackground = true)
@Composable
fun PreviewArchivedHabitCard() {
    val habit = Habit(
        id = "1",
        name = "Morning Run",
        description = "Go for a 30-minute run every morning.",
        iconEmoji = "🏃",
        themeColor = 0xFF5733,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        isActive = true,
        orderIndex = 0,
    )

    ReorderHabitCard(habit = habit, isDragging = false)
}

@Composable
fun ReorderHabitCard(
    habit: Habit,
    isDragging: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .semantics { selected = isDragging }
            .clip(CardDefaults.shape)
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = if (isDragging) CardDefaults.outlinedCardBorder()
            .copy(brush = SolidColor(MaterialTheme.colorScheme.primary)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top
            ) {
                HabitIcon(
                    color = Color(habit.themeColor),
                    habitIcon = habit.iconEmoji
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    HabitTitle(title = habit.name)
                    HabitDetails(habit.description)
                }
            }
        }
    }
}

@Composable
private fun HabitIcon(
    color: Color,
    habitIcon: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = CircleShape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(color.copy(.3f)))
    ) {
        Box(
            modifier = modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(habitIcon, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun HabitTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun HabitDetails(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}