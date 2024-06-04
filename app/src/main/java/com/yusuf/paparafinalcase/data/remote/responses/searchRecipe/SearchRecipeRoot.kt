package com.yusuf.paparafinalcase.data.remote.responses.searchRecipe

data class SearchRecipeRoot(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)