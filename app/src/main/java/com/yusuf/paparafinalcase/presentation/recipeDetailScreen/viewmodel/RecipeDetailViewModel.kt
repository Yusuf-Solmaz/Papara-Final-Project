package com.yusuf.paparafinalcase.presentation.recipeDetailScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import com.yusuf.paparafinalcase.data.remote.repository.getRecipeInformations.GetRecipeInformations
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.MainRandomFoodState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(val repository: GetRecipeInformations,private val foodDao: FoodDao): ViewModel(){

    private val _rootRecipeInformationResponse = MutableStateFlow(RecipeDetailState())
    val rootRecipeInformationResponse: Flow<RecipeDetailState> = _rootRecipeInformationResponse

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: Flow<Boolean> = _isFavorite



    fun getRecipeInformation(id: Int){
        viewModelScope.launch {
            repository.getRecipeInformations(id).collect{
                result ->
                when(result){
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
                        _rootRecipeInformationResponse.update { it.copy(
                            isLoading =  false,
                            rootResponse = result.data,
                            error = null
                            )
                        }
                        checkIfFavorite(id)
                    }
                }
            }
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
}