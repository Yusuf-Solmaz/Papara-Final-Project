package com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.remote.repository.randomRecipeRepo.RandomRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodScreenViewModel @Inject constructor(private val randomRecipeRepository: RandomRecipeRepository) : ViewModel() {
    private val _rootRandomFoodResponse = MutableStateFlow(RandomFoodState())
    val rootRandomFoodResponse: Flow<RandomFoodState> = _rootRandomFoodResponse

    init{
        getRandomFoods()
    }

    private fun getRandomFoods() {
        viewModelScope.launch {
            randomRecipeRepository.getRandomRecipes().collect {
                result ->
                when(result){
                    is RootResult.Error -> {
                        _rootRandomFoodResponse.update {
                            it.copy(
                                error = result.message,
                                isLoading = false,
                                rootResponse = null
                            )
                        }
                    }
                    RootResult.Loading -> {
                        _rootRandomFoodResponse.update {
                            it.copy(
                                isLoading = true,
                                rootResponse = null,
                                error = null
                            )
                        }
                    }
                    is RootResult.Success -> {
                        _rootRandomFoodResponse.update {
                            it.copy(
                                rootResponse = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }
}