package com.none.flow.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.none.flow.R

sealed class OnboardingPage(
    @DrawableRes
    val imageResId: Int,
    @StringRes
    val titleResId: Int,
    @StringRes
    val descriptionResId: Int
) {
    data object HabitFormation : OnboardingPage(
        imageResId = R.drawable.variant_formation,
        titleResId = R.string.onboarding_title_build_habits,
        descriptionResId = R.string.onboarding_message_build_habits
    )

    data object Consistency : OnboardingPage(
        imageResId = R.drawable.variant_consistency,
        titleResId = R.string.onboarding_title_stay_consistent,
        descriptionResId = R.string.onboarding_message_stay_consistent
    )

    data object HabitProgress : OnboardingPage(
        imageResId = R.drawable.variant_progress,
        titleResId = R.string.onboarding_title_track_progress,
        descriptionResId = R.string.onboarding_message_track_progress
    )
}
