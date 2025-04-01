package com.none.flow.data.model.enums

import com.none.flow.R

enum class ThemeMode(val labelResId: Int) {
    DAY(labelResId = R.string.theme_mode_light),
    NIGHT(labelResId = R.string.theme_mode_dark),
    SYSTEM(labelResId = R.string.theme_mode_system)
}
