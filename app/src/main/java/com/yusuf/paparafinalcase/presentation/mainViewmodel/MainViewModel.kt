package com.yusuf.paparafinalcase.presentation.mainViewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.data.datastore.MyPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore
): ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf("onboarding_screen")
        private set

    var isSplashScreenVisible by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {

            delay(2000)
            isSplashScreenVisible = false

            myPreferencesDataStore.readAppEntry.collect { loadOnBoardingScreen ->
                startDestination = if (loadOnBoardingScreen) {
                    "onboarding_screen"
                } else {
                    "main_screen"
                }
                isLoading = false
            }
        }
    }

    fun saveAppEntry() {
        viewModelScope.launch {
            myPreferencesDataStore.saveAppEntry()
        }
    }
}
