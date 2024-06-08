package com.yusuf.paparafinalcase.presentation.favoriteFoodScreen

import com.yusuf.paparafinalcase.data.local.model.LocalFoods

data class FavoriteFoodState(
    val isLoading: Boolean = false,
    val favoriteFoods: List<LocalFoods> = emptyList(),
    val error: String? = null
)

