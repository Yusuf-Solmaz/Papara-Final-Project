package com.yusuf.paparafinalcase.presentation.foodScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.data.remote.responses.recipe.Recipe
import com.yusuf.paparafinalcase.data.remote.responses.searchRecipe.Result
import com.yusuf.paparafinalcase.presentation.components.LazyColumnRecipeItem
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.FoodScreenViewModel
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.RandomFoodState
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.SearchRecipeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen (navController: NavController,category: String?,viewModel: FoodScreenViewModel = hiltViewModel()) {

    val randomFoodState by viewModel.rootRandomFoodResponse.collectAsState(RandomFoodState())
    val searchRecipeState by viewModel.rootSearchRecipeResponse.collectAsState(SearchRecipeState())

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        Log.i("Category", "FoodScreen: $category")
    }

    if (randomFoodState.error != null){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = randomFoodState.error.toString())
        }
    }


    if (randomFoodState.isLoading) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingLottie(R.raw.general_loading_lottie)
        }
    }
     else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Food Page") }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {

                    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newText ->
                                searchQuery = newText
                            },
                            label = { Text("Enter querry") },
                            placeholder = { Text("Placeholder") },
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .weight(3f)
                        )

                        Button(
                            onClick = {
                                viewModel.searchRecipe(searchQuery, null, null)
                            }, modifier = Modifier.weight(1f)

                        ) {
                            Text(text = "Filter")
                        }
                    }



                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        val searchResults = searchRecipeState.rootResponse?.results
                        if (!searchResults.isNullOrEmpty()) {
                            items(searchResults.size) { index ->
                                val searchRecipe = searchResults[index]
                                SearchFoodItem(searchRecipe){
                                    navController.navigate("recipe_detail_page/${searchRecipe.id}")
                                }
                            }
                        }
                        else {
                            val randomRecipes = randomFoodState.rootResponse?.recipes
                            if (!randomRecipes.isNullOrEmpty()) {
                                items(randomRecipes.size) { index ->
                                    val randomRecipe = randomRecipes[index]
                                    RandomFoodItem(randomRecipe){
                                        navController.navigate("recipe_detail_page/${randomRecipe.id}")
                                    }
                                }
                            }
                        }
                    }
                }
            })

       }

}



@Composable
fun RandomFoodItem(randomFood: Recipe,onCardClick: () ->Unit) {
    LazyColumnRecipeItem(image = randomFood.image, title = randomFood.title,onCardClick)
}

@Composable
fun SearchFoodItem(searchFood: Result,onCardClick: () ->Unit) {
    LazyColumnRecipeItem(image = searchFood.image, title = searchFood.title,onCardClick)
}

