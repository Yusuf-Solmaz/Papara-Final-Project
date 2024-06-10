package com.yusuf.paparafinalcase.presentation.foodScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.data.remote.responses.recipe.Recipe
import com.yusuf.paparafinalcase.data.remote.responses.searchRecipe.Result
import com.yusuf.paparafinalcase.presentation.components.LazyColumnRecipeItem
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.FoodScreenViewModel
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.RandomFoodState
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.SearchRecipeState
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(navController: NavController, category: String?, viewModel: FoodScreenViewModel = hiltViewModel()) {

    val randomFoodState by viewModel.rootRandomFoodResponse.collectAsState(RandomFoodState())
    val searchRecipeState by viewModel.rootSearchRecipeResponse.collectAsState(SearchRecipeState())

    val supportedCuisines = listOf(
        "African", "Asian", "American", "British", "Cajun", "Caribbean", "Chinese",
        "Eastern European", "European", "French", "German", "Greek", "Indian", "Irish",
        "Italian", "Japanese", "Jewish", "Korean", "Latin American", "Mediterranean",
        "Mexican", "Middle Eastern", "Nordic", "Southern", "Spanish", "Thai", "Vietnamese"
    )

    val supportedDiets = listOf(
        "Gluten Free", "Ketogenic", "Vegetarian", "Lacto-Vegetarian", "Ovo-Vegetarian",
        "Vegan", "Pescetarian", "Paleo", "Primal", "Low FODMAP", "Whole30"
    )

    var isCuisineDropdownOpen by remember { mutableStateOf(false) }
    var isDietDropdownOpen by remember { mutableStateOf(false) }
    var selectedCuisine by remember { mutableStateOf<String?>(null) }
    var selectedDiet by remember { mutableStateOf<String?>(null) }

    var searchQuery by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        Log.i("Category", "FoodScreen: $category")
    }

    if (randomFoodState.error != null) {
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
    } else {
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
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newText -> searchQuery = newText },
                            label = { Text("Enter query") },
                            placeholder = { Text("Placeholder") },
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .weight(3f)
                        )

                        Button(
                            onClick = {
                                isSheetOpen = true
                            },
                            modifier = Modifier.weight(1f)
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
                                SearchFoodItem(searchRecipe) {
                                    navController.navigate("recipe_detail_page/${searchRecipe.id}")
                                }
                            }
                        } else {
                            val randomRecipes = randomFoodState.rootResponse?.recipes
                            if (!randomRecipes.isNullOrEmpty()) {
                                items(randomRecipes.size) { index ->
                                    val randomRecipe = randomRecipes[index]
                                    RandomFoodItem(randomRecipe) {
                                        navController.navigate("recipe_detail_page/${randomRecipe.id}")
                                    }
                                }
                            }
                        }
                    }

                    if (isSheetOpen) {
                        ModalBottomSheet(
                            modifier = Modifier
                                .padding(bottom = paddingValues.calculateBottomPadding())
                                .size(300.dp, 300.dp),
                            sheetState = sheetState,
                            onDismissRequest = {
                                isSheetOpen = false
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .clickable { isCuisineDropdownOpen = !isCuisineDropdownOpen }
                                                .padding(16.dp)
                                                .size(150.dp, 50.dp)
                                        ) {
                                            Text(text = selectedCuisine ?: "Select Cuisine")
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Drop-Down Arrow",
                                                tint = Color.Black
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = isCuisineDropdownOpen,
                                            onDismissRequest = { isCuisineDropdownOpen = false },
                                            modifier = Modifier.size(150.dp, 300.dp)
                                        ) {
                                            supportedCuisines.forEach { cuisine ->
                                                DropdownMenuItem(
                                                    text = { Text(cuisine) },
                                                    onClick = {
                                                        selectedCuisine = cuisine
                                                        isCuisineDropdownOpen = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Box(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .clickable { isDietDropdownOpen = !isDietDropdownOpen }
                                                .padding(16.dp)
                                                .size(150.dp, 50.dp)
                                        ) {
                                            Text(text = selectedDiet ?: "Select Diet")
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Drop-Down Arrow",
                                                tint = Color.Black
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = isDietDropdownOpen,
                                            onDismissRequest = { isDietDropdownOpen = false },
                                            modifier = Modifier.size(150.dp, 300.dp)
                                        ) {
                                            supportedDiets.forEach { diet ->
                                                DropdownMenuItem(
                                                    text = { Text(diet) },
                                                    onClick = {
                                                        selectedDiet = diet
                                                        isDietDropdownOpen = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        viewModel.searchRecipe(query = searchQuery, diet = selectedDiet, cuisine = selectedCuisine?.lowercase(Locale.getDefault()))
                                        isSheetOpen = false
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(16.dp)
                                ) {
                                    Text(text = "Filter")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

/*
*
                       */



@Composable
fun RandomFoodItem(randomFood: Recipe, onCardClick: () -> Unit) {
    LazyColumnRecipeItem(image = randomFood.image, title = randomFood.title, onCardClick)
}

@Composable
fun SearchFoodItem(searchFood: Result, onCardClick: () -> Unit) {
    LazyColumnRecipeItem(image = searchFood.image, title = searchFood.title, onCardClick)
}
