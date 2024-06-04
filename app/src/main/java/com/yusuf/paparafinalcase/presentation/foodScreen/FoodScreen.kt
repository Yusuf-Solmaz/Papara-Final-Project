package com.yusuf.paparafinalcase.presentation.foodScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.data.remote.responses.recipe.Recipe
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.FoodScreenViewModel
import com.yusuf.paparafinalcase.presentation.foodScreen.viewmodel.RandomFoodState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen (viewModel: FoodScreenViewModel = hiltViewModel()) {

    val randomFoodState by viewModel.rootRandomFoodResponse.collectAsState(RandomFoodState())


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

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        randomFoodState.rootResponse?.recipes?.let {
                            items(it.size) { index ->
                                val randomRecipe = randomFoodState.rootResponse!!.recipes[index]
                                RandomFoodItem(randomRecipe)
                            }
                        }
                    }

                }

            }
        )
    }
}

@Composable
fun RandomFoodItem(randomFood: Recipe) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_food_image_lottie))

    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SubcomposeAsyncImage(
                model = randomFood.image,
                contentDescription = null,
                modifier = Modifier.size(100.dp, 100.dp),
                loading = {
                    LottieAnimation(
                        composition,
                        modifier = Modifier.size(100.dp),
                        iterations = Int.MAX_VALUE
                    )
                }
            )

            Text(
                text = randomFood.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
