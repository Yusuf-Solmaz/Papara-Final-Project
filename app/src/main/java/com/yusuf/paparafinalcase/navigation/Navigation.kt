package com.yusuf.paparafinalcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yusuf.paparafinalcase.presentation.foodScreen.FoodScreen
import com.yusuf.paparafinalcase.presentation.mainScreen.MainScreen
import com.yusuf.paparafinalcase.presentation.recipeDetailScreen.RecipeDetailScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {

        composable("main_screen"){
            MainScreen(navController)
        }

        composable("food_screen/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType})
            ) {
            val category = it.arguments?.getString("category")
            category?.let { sendCategory -> FoodScreen(navController, sendCategory) }
        }

        composable("recipe_detail_page/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ){
            val recipeId = it.arguments?.getInt("recipeId")
            recipeId?.let { sendRecipeId -> RecipeDetailScreen(navController, sendRecipeId) }
        }
    }


}