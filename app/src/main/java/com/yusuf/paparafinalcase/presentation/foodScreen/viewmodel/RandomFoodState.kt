package com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel

import com.yusuf.paparafinalcase.data.remote.responses.recipe.RandomRecipeRoot

data class RandomFoodState (
    val isLoading: Boolean = false,
    val rootResponse: RandomRecipeRoot? =null,
    val error: String?= null
    )