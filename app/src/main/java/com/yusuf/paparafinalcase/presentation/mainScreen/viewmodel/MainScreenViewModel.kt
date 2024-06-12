package com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.local.repository.DailyRecommendationRepository
import com.yusuf.paparafinalcase.data.remote.repository.searchRecipeRepository.SearchRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val searchRecipeRepository: SearchRecipeRepository,
    private val dailyRecommendationRepository: DailyRecommendationRepository
) : ViewModel() {

    private val _rootMainSearchRecipeResponse = MutableStateFlow(MainSearchRecipeState())
    val rootMainSearchRecipeResponse: Flow<MainSearchRecipeState> = _rootMainSearchRecipeResponse

    private val _dailyRecommendationState = MutableStateFlow(DailyRecommendationState())
    val dailyRecommendationState: Flow<DailyRecommendationState> = _dailyRecommendationState


    fun searchRecipe(query: String, diet: String?, cuisine: String?) {
        viewModelScope.launch {
            searchRecipeRepository.searchRecipes(query, diet, cuisine).collect { result ->
                when (result) {
                    is RootResult.Error -> {
                        _rootMainSearchRecipeResponse.update {
                            it.copy(
                                error = result.message,
                                isLoading = false,
                                rootResponse = null
                            )
                        }
                    }

                    RootResult.Loading -> {
                        _rootMainSearchRecipeResponse.update {
                            it.copy(
                                isLoading = true,
                                rootResponse = null,
                                error = null
                            )
                        }
                    }

                    is RootResult.Success -> {
                        _rootMainSearchRecipeResponse.update {
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


    fun getDailyRecommendation() {
        viewModelScope.launch(Dispatchers.IO) {
            dailyRecommendationRepository.getDailyRecommendation().collect { recommendation ->
                _dailyRecommendationState.update {
                    it.copy(
                        recommendation = recommendation,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}