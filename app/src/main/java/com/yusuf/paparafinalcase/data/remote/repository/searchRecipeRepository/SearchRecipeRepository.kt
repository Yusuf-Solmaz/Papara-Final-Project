package com.yusuf.paparafinalcase.data.remote.repository.searchRecipeRepository

import com.yusuf.paparafinalcase.core.rootFlow.rootFlow
import com.yusuf.paparafinalcase.data.remote.network.ApiService
import javax.inject.Inject

class SearchRecipeRepository @Inject constructor(val apiService: ApiService) {
    fun searchRecipes(query: String?, diet: String?, cuisine: String?) = rootFlow {
        apiService.searchRecipes(query = query, diet = diet, cuisine = cuisine)
    }
}