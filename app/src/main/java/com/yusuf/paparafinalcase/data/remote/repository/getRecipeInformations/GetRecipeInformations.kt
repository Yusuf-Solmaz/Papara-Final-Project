package com.yusuf.paparafinalcase.data.remote.repository.getRecipeInformations

import com.yusuf.paparafinalcase.core.rootFlow.rootFlow
import com.yusuf.paparafinalcase.data.remote.network.ApiService
import javax.inject.Inject

class GetRecipeInformations @Inject constructor(val apiService: ApiService) {

    fun getRecipeInformations(id: Int) = rootFlow {
        apiService.getRecipeInformation(id)
    }
}