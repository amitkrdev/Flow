package com.none.flow.data.model

import com.none.flow.data.model.enums.ThemeMode

data class UserPreferences(
    val theme: ThemeMode,
    val enableDynamicColor: Boolean
)
