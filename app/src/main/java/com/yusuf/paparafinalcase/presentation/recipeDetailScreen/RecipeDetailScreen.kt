package com.yusuf.paparafinalcase.presentation.recipeDetailScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab

import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.data.mapper.toLocalFoods
import com.yusuf.paparafinalcase.data.remote.responses.recipe.ExtendedIngredient
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.recipeDetailScreen.viewmodel.RecipeDetailState
import com.yusuf.paparafinalcase.presentation.recipeDetailScreen.viewmodel.RecipeDetailViewModel
import com.yusuf.paparafinalcase.ui.theme.Orange
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(navController: NavController,recipeId: Int, viewModel: RecipeDetailViewModel = hiltViewModel()) {

    val foodState by viewModel.rootRecipeInformationResponse.collectAsState(RecipeDetailState())
    val isFavorite by viewModel.isFavorite.collectAsState(false)

    LaunchedEffect(key1 = recipeId) {
        viewModel.getRecipeInformation(id = recipeId)
        Log.d("RecipeDetailScreen", "LaunchedEffect: $recipeId")
    }
    if (foodState.isLoading){
       Column(
           Modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {

       }
        LoadingLottie(resId = R.raw.general_loading_lottie)
    }
    if (foodState.error != null){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                foodState.error ?: "",
            )
        }
    }
    else{

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                navController.navigate("main_screen")

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Back"
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = foodState.rootResponse?.title ?: "",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    },
                    actions = {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(end = 10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                tint = Color.Red,
                                painter = painterResource(id = if (isFavorite) R.drawable.fav_food else R.drawable.not_fav_food),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 10.dp, start = 10.dp)
                                    .clickable {
                                        foodState.rootResponse
                                            ?.toLocalFoods()
                                            ?.let { viewModel.addOrRemoveFavorite(it) }
                                    }
                            )
                        }
                    }
                )
            },
            content = {
                paddingValues->
                Column (
                    modifier = Modifier
                        .fillMaxSize()

                ){

                    FoodDetailImage(image = foodState.rootResponse?.image ?: "")
                    FoodContents(foodState)
                    RecipeTabRow(foodState)
                    Instructions(foodState,paddingValues)

                }
            }
        )
    }
}

@Composable
fun FoodContents(foodState: RecipeDetailState){
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Icon(
                painter = if (foodState.rootResponse?.glutenFree == true) painterResource(id = R.drawable.gluten_free_icon) else painterResource(id = R.drawable.gluten_icon),
                contentDescription = "Gluten Free",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = if (foodState.rootResponse?.glutenFree == true) "Gluten Free" else "Includes Gluten",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        if (foodState.rootResponse?.vegetarian == true) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vegetarian_icon),
                    contentDescription = "Vegetarian",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Vegetarian",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        if (foodState.rootResponse?.veryHealthy == true) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.healthy_icon),
                    contentDescription = "Healthy",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Healthy",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun Instructions(foodState: RecipeDetailState, paddingValues: PaddingValues) {
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = "Instructions",
        style = TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            Text(
                text = foodState.rootResponse?.instructions ?: "",
                overflow = TextOverflow.Visible
            )
        }
    }
}



@Composable
fun IngredientItem(ingredient: ExtendedIngredient) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bullet),
            contentDescription = null,
            modifier = Modifier.size(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = ingredient.name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeTabRow(foodState: RecipeDetailState) {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(2.dp),
                        color = Color.White
                    )
                }
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    modifier = Modifier
                        .background(if (pagerState.currentPage == 0) Orange else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Details",
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == 0) Color.White else Color.Gray
                        )
                    )
                }

                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    modifier = Modifier
                        .background(if (pagerState.currentPage == 1) Orange else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Ingredients",
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == 1) Color.White else Color.Gray
                        )
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (foodState.rootResponse?.cookingMinutes != 0 && foodState.rootResponse?.preparationMinutes != 0 )
                        {

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                            if (foodState.rootResponse?.cookingMinutes != 0){
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_microwave), contentDescription = "", modifier = Modifier.size(50.dp))
                                    Text(
                                        text = foodState.rootResponse?.cookingMinutes.toString(),
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            if (foodState.rootResponse?.preparationMinutes != 0){
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.clock), contentDescription = "", modifier = Modifier.size(50.dp))
                                    Text(
                                        text = foodState.rootResponse?.preparationMinutes.toString(),
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                        }

                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(painter = painterResource(id = R.drawable.ic_serving), contentDescription = "", modifier = Modifier.size(50.dp))
                                Text(
                                    text = "For ${foodState.rootResponse?.servings.toString()} People",
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(painter = painterResource(id = R.drawable.ic_like_new), contentDescription = "", modifier = Modifier.size(50.dp))
                                Text(
                                    text = foodState.rootResponse?.aggregateLikes.toString(),
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            items(foodState.rootResponse?.extendedIngredients ?: emptyList()) { ingredient ->
                                IngredientItem(ingredient)
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDetailImage(image: String){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_food_image_lottie))

    SubcomposeAsyncImage(
        model = image,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        loading = {
            LottieAnimation(
                composition,
                modifier = Modifier.size(100.dp),
                iterations = Int.MAX_VALUE
            )
        }
    )
}
