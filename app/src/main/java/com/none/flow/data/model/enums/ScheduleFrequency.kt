package com.none.flow.data.model.enums

import androidx.annotation.PluralsRes
import com.none.flow.R

enum class ScheduleFrequency(@PluralsRes val labelResId: Int) {
    DAILY(labelResId = R.plurals.habit_interval_daily),
    WEEKLY(labelResId = R.plurals.habit_interval_weekly)
}