package com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel

import com.yusuf.paparafinalcase.data.remote.responses.searchRecipe.SearchRecipeRoot

data class SearchRecipeState(
    val isLoading: Boolean = false,
    val rootResponse: SearchRecipeRoot? =null,
    val error: String?= null
)
