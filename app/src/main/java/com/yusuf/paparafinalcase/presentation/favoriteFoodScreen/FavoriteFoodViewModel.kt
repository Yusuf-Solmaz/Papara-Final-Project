package com.yusuf.paparafinalcase.presentation.favoriteFoodScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFoodViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {

    private val _favoriteFoodsState = MutableStateFlow(FavoriteFoodState())
    val favoriteFoodsState: Flow<FavoriteFoodState> = _favoriteFoodsState

    init {
        fetchFavoriteFoods()
    }

    private fun fetchFavoriteFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteFoodsState.update { it.copy(isLoading = true, error = null) }
            try {
                foodDao.getFoods().collect { favoriteFoods ->
                    _favoriteFoodsState.update {
                        it.copy(isLoading = false, favoriteFoods = favoriteFoods, error = null)
                    }
                }
            } catch (e: Exception) {
                _favoriteFoodsState.update {
                    it.copy(isLoading = false, error = e.message, favoriteFoods = emptyList())
                }
            }
        }
    }

    fun deleteFavoriteFood(foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                foodDao.deleteFood(foodId)
                fetchFavoriteFoods()
            } catch (e: Exception) {
                _favoriteFoodsState.update {
                    it.copy(error = e.message, favoriteFoods = emptyList(), isLoading = false)
                }
            }
        }
    }
}