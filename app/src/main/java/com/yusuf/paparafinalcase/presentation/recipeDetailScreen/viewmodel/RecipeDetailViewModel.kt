package com.yusuf.paparafinalcase.presentation.recipeDetailScreen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.constants.ConnectivityUtil.isInternetAvailable
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import com.yusuf.paparafinalcase.data.mapper.toRecipeInfoRoot
import com.yusuf.paparafinalcase.data.remote.repository.getRecipeInformations.GetRecipeInformations
import com.yusuf.paparafinalcase.data.remote.responses.recipe.AnalyzedInstruction
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(val repository: GetRecipeInformations, private val foodDao: FoodDao,@ApplicationContext private val context: Context): ViewModel() {

    private val _rootRecipeInformationResponse = MutableStateFlow(RecipeDetailState())
    val rootRecipeInformationResponse: Flow<RecipeDetailState> = _rootRecipeInformationResponse

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: Flow<Boolean> = _isFavorite

    fun getRecipeInformation(id: Int) {
        viewModelScope.launch {
            if (isInternetAvailable(context)) {
                fetchRecipeFromNetwork(id)
            } else {
                fetchRecipeFromDatabase(id)
            }
        }
    }

    private suspend fun fetchRecipeFromNetwork(id: Int) {
        repository.getRecipeInformations(id).collect { result ->
            when(result) {
                is RootResult.Error -> {
                    _rootRecipeInformationResponse.update {
                        it.copy(
                            error = result.message,
                            isLoading = false,
                            rootResponse = null
                        )
                    }
                }
                RootResult.Loading -> {
                    _rootRecipeInformationResponse.update {
                        it.copy(
                            isLoading = true,
                            rootResponse = null,
                            error = null
                        )
                    }
                }
                is RootResult.Success -> {
                    val parsedInstructions = result.data?.let { parseInstructions(it.analyzedInstructions) }
                    val updatedRootResponse = parsedInstructions?.let { result.data.copy(instructions = it) }

                    _rootRecipeInformationResponse.update {
                        it.copy(
                            isLoading = false,
                            rootResponse = updatedRootResponse,
                            error = null
                        )
                    }
                    checkIfFavorite(id)
                }
            }
        }
    }

    private fun parseInstructions(analyzedInstructions: List<AnalyzedInstruction>): String {
        val steps = analyzedInstructions.flatMap { it.steps }
        return steps.joinToString(separator = "\n") { step ->
            "${step.number}. ${step.step}"
        }
    }

    private fun checkIfFavorite(foodId: Int) {
        viewModelScope.launch {
            _isFavorite.value = foodDao.isFoodInFavorites(foodId) > 0
        }
    }

    fun addOrRemoveFavorite(food: LocalFoods) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                foodDao.deleteFood(food.foodId)
            } else {
                foodDao.insertFood(food)
            }
            _isFavorite.value = !_isFavorite.value
        }
    }

    private fun fetchRecipeFromDatabase(foodId: Int) {
        viewModelScope.launch {
            val food = foodDao.getFoodById(foodId)
            if (food != null) {
                _rootRecipeInformationResponse.update {
                    it.copy(
                        isLoading = false,
                        rootResponse = food.toRecipeInfoRoot(),
                        error = null
                    )
                }
                _isFavorite.value = true
            } else {
                _rootRecipeInformationResponse.update {
                    it.copy(
                        isLoading = false,
                        rootResponse = null,
                        error = "No data available offline."
                    )
                }
            }
        }
    }
}

