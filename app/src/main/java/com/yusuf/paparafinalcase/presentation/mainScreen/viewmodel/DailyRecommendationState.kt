package com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel

import com.yusuf.paparafinalcase.data.local.model.DailyRecommendation

data class DailyRecommendationState(
    val isLoading: Boolean = false,
    val recommendation: DailyRecommendation? = null,
    val error: String? = null
)