package com.yusuf.paparafinalcase.data.remote.repository.randomRecipeRepo

import com.yusuf.paparafinalcase.core.rootFlow.rootFlow
import com.yusuf.paparafinalcase.data.remote.network.ApiService
import javax.inject.Inject

class RandomRecipeRepository @Inject constructor(val apiService: ApiService) {

    fun getRandomRecipes() = rootFlow {
        apiService.getRandomRecipes()
    }

}