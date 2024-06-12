package com.yusuf.paparafinalcase.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.foodScreen.FoodScreen
import com.yusuf.paparafinalcase.presentation.mainScreen.MainScreen
import com.yusuf.paparafinalcase.presentation.mainViewmodel.MainViewModel
import com.yusuf.paparafinalcase.presentation.onBoardingScreen.OnBoardingScreen
import com.yusuf.paparafinalcase.presentation.recipeDetailScreen.RecipeDetailScreen
import com.yusuf.paparafinalcase.presentation.splashScreen.SplashScreen

@Composable
fun Navigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    LaunchedEffect(mainViewModel.isLoading) {
        if (!mainViewModel.isLoading) {
            navController.navigate(mainViewModel.startDestination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    if (mainViewModel.isSplashScreenVisible) {
        SplashScreen()
    } else if (mainViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingLottie(resId = R.raw.general_loading_lottie)
        }
    }

    else {
        NavHost(navController = navController, startDestination = mainViewModel.startDestination) {
            composable("onboarding_screen") {
                OnBoardingScreen(mainViewModel, navController)
            }
            composable("main_screen") {
                MainScreen(navController)
            }
            composable(
                "food_screen/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) {
                val category = it.arguments?.getString("category")
                category?.let { sendCategory -> FoodScreen(navController, sendCategory) }
            }
            composable(
                "recipe_detail_page/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) {
                val recipeId = it.arguments?.getInt("recipeId")
                recipeId?.let { sendRecipeId -> RecipeDetailScreen(navController, sendRecipeId) }
            }
        }
    }
}

