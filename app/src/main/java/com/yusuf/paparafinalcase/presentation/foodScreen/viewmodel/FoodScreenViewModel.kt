package com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.remote.repository.randomRecipeRepo.RandomRecipeRepository
import com.yusuf.paparafinalcase.data.remote.repository.searchRecipeRepository.SearchRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodScreenViewModel @Inject constructor(private val randomRecipeRepository: RandomRecipeRepository,private val searchRecipeRepository: SearchRecipeRepository) : ViewModel() {
    private val _rootRandomFoodResponse = MutableStateFlow(RandomFoodState())
    val rootRandomFoodResponse: Flow<RandomFoodState> = _rootRandomFoodResponse

    private val _rootSearchRecipeResponse = MutableStateFlow(SearchRecipeState())
    val rootSearchRecipeResponse: Flow<SearchRecipeState> = _rootSearchRecipeResponse


    init{
        getRandomFoods()
    }

    fun searchRecipe(query: String?, diet: String?, cuisine: String?) {
        viewModelScope.launch {
            searchRecipeRepository.searchRecipes(query, diet, cuisine).collect { result ->
                when (result) {
                    is RootResult.Error -> {
                        _rootSearchRecipeResponse.update {
                            it.copy(
                                error = result.message,
                                isLoading = false,
                                rootResponse = null
                            )
                        }
                    }

                    RootResult.Loading -> {
                        _rootSearchRecipeResponse.update {
                            it.copy(
                                isLoading = true,
                                rootResponse = null,
                                error = null
                            )
                        }
                    }

                    is RootResult.Success -> {
                        _rootSearchRecipeResponse.update {
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

            private fun getRandomFoods() {
                viewModelScope.launch {
                    randomRecipeRepository.getRandomRecipes(10).collect { result ->
                        when (result) {
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