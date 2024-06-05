package com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel

import com.yusuf.paparafinalcase.data.remote.responses.searchRecipe.SearchRecipeRoot

data class MainSearchRecipeState(
    val isLoading: Boolean = false,
    val rootResponse: SearchRecipeRoot? =null,
    val error: String?= null
)
