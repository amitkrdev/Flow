package com.none.flow.presentation.screens.dashboard.components

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.none.flow.data.model.HabitSchedule
import com.none.flow.data.model.HabitStreak
import com.none.flow.data.model.UserHabit
import com.none.flow.data.model.enums.ScheduleFrequency
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
fun PreviewHabitCard() {
    val habit = UserHabit(
        id = "1",
        name = "Morning Run",
        description = "Go for a 30-minute run every morning.",
        iconEmoji = "🏃",
        themeColor = 0xFF5733,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        isActive = true,
        orderIndex = 0,
        schedule = HabitSchedule(
            habitId = "1",
            recurrenceDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            repeatInterval = ScheduleFrequency.DAILY,
            repeatIntervalCount = 1,
            dailyGoal = 2,
            endDate = null,
            startDate = Instant.now()
        ),
        streak = HabitStreak(
            habitId = "1",
            activeStreakCount = 1,
            maxStreakCount = 1,
            lastCompletionDate = LocalDate.now(),
            lastSkippedDate = LocalDate.now(),
            completionCount = 0
        ),
        logs = emptyList()
    )

    HabitCard(habit = habit, onHabitClick = {}, onHabitLongPress = {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitCard(
    habit: UserHabit,
    onHabitClick: () -> Unit,
    onHabitLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = onHabitClick,
                onLongClick = onHabitLongPress
            )
            .clip(CardDefaults.shape)
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
                HabitCompletionChip(
                    color = Color(habit.themeColor),
                    goalCount = habit.schedule.dailyGoal,
                    completedCount = 1
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HabitProgressBar(
                color = Color(habit.themeColor),
                goalCount = habit.schedule.dailyGoal,
                completedCount = 1
            )
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

@Composable
fun HabitProgressBar(
    color: Color,
    goalCount: Int,
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    val progress = remember {
        derivedStateOf { completedCount / goalCount.toFloat() }
    }

    LinearProgressIndicator(
        progress = { progress.value },
        modifier = modifier.fillMaxWidth(),
        color = color.copy(alpha = 0.7f),
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Composable
fun HabitCompletionChip(
    color: Color,
    goalCount: Int,
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.7f),
        contentColor = Color.White,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$completedCount / $goalCount",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}