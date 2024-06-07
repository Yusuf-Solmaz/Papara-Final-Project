package com.yusuf.paparafinalcase.presentation.recipeDetailScreen.viewmodel

import com.yusuf.paparafinalcase.data.remote.responses.recipe.RecipeInfoRoot


data class RecipeDetailState(
    val isLoading: Boolean = false,
    val rootResponse: RecipeInfoRoot? =null,
    val error: String?= null
)
