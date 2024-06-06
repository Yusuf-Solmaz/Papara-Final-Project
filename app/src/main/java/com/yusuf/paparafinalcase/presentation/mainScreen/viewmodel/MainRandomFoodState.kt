package com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel

import com.yusuf.paparafinalcase.data.remote.responses.recipe.RandomRecipeRoot

data class MainRandomFoodState (
    val isLoading: Boolean = false,
    val rootResponse: RandomRecipeRoot? =null,
    val error: String?= null
)
