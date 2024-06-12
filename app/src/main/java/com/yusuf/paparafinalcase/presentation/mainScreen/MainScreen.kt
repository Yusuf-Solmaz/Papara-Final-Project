package com.yusuf.paparafinalcase.presentation.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.core.constants.Constants.APP_NAME
import com.yusuf.paparafinalcase.core.constants.Constants.FAVORITE_SCREEN_TITLE
import com.yusuf.paparafinalcase.core.constants.Constants.SEARCH_SCREEN_TITLE
import com.yusuf.paparafinalcase.core.constants.Constants.categories
import com.yusuf.paparafinalcase.core.constants.Constants.categoryImages
import com.yusuf.paparafinalcase.presentation.components.LazyColumnRecipeItem
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.favoriteFoodScreen.FavoriteFoodScreen
import com.yusuf.paparafinalcase.presentation.foodScreen.FoodScreen
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.DailyRecommendationState
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.MainScreenViewModel
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.MainSearchRecipeState
import com.yusuf.paparafinalcase.ui.theme.BottomBarUnselectedIcon
import com.yusuf.paparafinalcase.ui.theme.Orange
import com.yusuf.paparafinalcase.ui.theme.UnSelectedBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val mainSearchRecipeState by viewModel.rootMainSearchRecipeResponse.collectAsState(MainSearchRecipeState())

    val dailyRecommendationState by viewModel.dailyRecommendationState.collectAsState(DailyRecommendationState())

    val items = listOf(APP_NAME, SEARCH_SCREEN_TITLE, FAVORITE_SCREEN_TITLE)
    val chosenItem = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        viewModel.getOneRandomFood()
        viewModel.getDailyRecommendation()
    }

    Scaffold(
        topBar = {
            val title = when (chosenItem.value) {
                0 -> APP_NAME
                1 -> SEARCH_SCREEN_TITLE
                2 -> FAVORITE_SCREEN_TITLE
                else -> "Food App"
            }
            TopAppBar(
                title = { Text(text = title, style = TextStyle(
                    color = Orange,
                    fontFamily = FontFamily(Font(R.font.onboarding_title1)),
                    fontSize = 20.sp
                )) },
                actions = {
                    IconButton(onClick = {}, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Orange
                    )) {
                        Icon(Icons.Default.AccountCircle,contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValues ->
            if (chosenItem.value == 0){
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    SearchBar(
                        stateValue = searchQuery,
                        onValueChange = { updatedQuery -> searchQuery = updatedQuery },
                        label = "Search Recipes",
                        isSearching = isSearching,
                        onSearchClicked = {
                            isSearching = !isSearching
                            if (isSearching) {
                                viewModel.searchRecipe(query = searchQuery, null, null)
                            }
                        },
                        onSearching = {
                            viewModel.searchRecipe(query = searchQuery, null, null)
                        },
                        onClearSearch = {
                            isSearching = false
                            searchQuery = ""
                        }
                    )
                    if (isSearching) {
                        if (mainSearchRecipeState.isLoading) {
                            LoadingLottie(R.raw.general_loading_lottie)
                        } else if (mainSearchRecipeState.error != null) {
                            Text(text = mainSearchRecipeState.error.toString())
                        } else {
                            mainSearchRecipeState.rootResponse?.let { searchRecipeRoot ->
                                LazyColumn {
                                    items(searchRecipeRoot.results) { result ->
                                        LazyColumnRecipeItem(
                                            image = result.image,
                                            title = result.title,
                                            onCardClick = {
                                                navController.navigate("recipe_detail_page/${result.id}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        PremiumPromotion()
                        CategorySection(navController)
                        if (dailyRecommendationState.recommendation != null){
                            Recommendations(dailyRecommendationState,navController)
                        }

                    }
                }
            }
            if (chosenItem.value == 1){
                FoodScreen(navController = navController, category = null)
            }
            if (chosenItem.value == 2){
                FavoriteFoodScreen(navController)
            }

        },
        bottomBar = {
            NavigationBar(containerColor =  Color.White, modifier = Modifier.height(100.dp)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = chosenItem.value == index,
                        onClick = { chosenItem.value = index },
                        colors = NavigationBarItemDefaults.colors(

                          unselectedIconColor = BottomBarUnselectedIcon,
                          unselectedTextColor = BottomBarUnselectedIcon,
                          selectedIconColor = Orange,
                          selectedTextColor = Orange,
                          indicatorColor = Color.White
                        ),
                        icon = {
                            when(item){
                                APP_NAME -> {Icon(painter = painterResource(id = R.drawable.home), contentDescription = null)}
                                SEARCH_SCREEN_TITLE -> {Icon(painter = painterResource(id = R.drawable.search), contentDescription = null)}
                                FAVORITE_SCREEN_TITLE -> {Icon(painter = painterResource(id = R.drawable.favorite), contentDescription = null)}
                            }

                        },
                        label = {
                            if (item == APP_NAME){ Text("Home")} else Text(text = item)
                        }

                    )
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    stateValue: String,
    label: String,
    onValueChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchClicked: () -> Unit,
    onSearching: () -> Unit,
    onClearSearch: () -> Unit
) {
    var textValue by remember { mutableStateOf(stateValue) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        label = { Text(text = label) },
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChange(it)
            onSearching()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "")
        },
        trailingIcon = {
            if (isSearching) {
                IconButton(onClick = {
                    onClearSearch()
                    focusManager.clearFocus()
                }) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    onSearchClicked()
                }
            },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedLeadingIconColor = Orange,
            focusedLeadingIconColor = Orange,
            disabledBorderColor = Orange,
            focusedBorderColor = Orange,
            unfocusedBorderColor = UnSelectedBG,
            cursorColor = Orange,
            unfocusedLabelColor = UnSelectedBG,
            focusedLabelColor = Orange
        )
    )
}

@Composable
fun PremiumPromotion() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.trial_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Go to premium now!",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Text(
                    text = "Get access to all the amazing recipes from the world",
                    color = Color.White
                )
                Button(
                    onClick = {  },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Start 7-day FREE Trial")
                }
            }
        }
    }
}

@Composable
fun CategorySection(navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Category", style = MaterialTheme.typography.displaySmall)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories.size) { index ->
                CategoryItem(
                    category = categories[index],
                    onClick = { categoryName ->
                        navController.navigate("food_screen/$categoryName")
                    }
                )
            }
        }
    }
}


@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    val imageRes = categoryImages[category] ?: R.drawable.trial_bg

    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable {
                onClick(category)
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = category,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun Recommendations(dailyRecommendationState:DailyRecommendationState,navController: NavController) {
    val randomFood = dailyRecommendationState.recommendation


    if (dailyRecommendationState.isLoading){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
            LoadingLottie(R.raw.general_loading_lottie)
        }
        LoadingLottie(R.raw.general_loading_lottie)
    }
    if (dailyRecommendationState.error != null){
        Text(text = dailyRecommendationState.error.toString())
    }
    else{
        if (randomFood != null){


    Column {
        Text(text = "Daily Recommendation", style = MaterialTheme.typography.displaySmall)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    navController.navigate("recipe_detail_page/${randomFood.foodId}")
                }
        ) {
            Box {

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_food_image_lottie))

                SubcomposeAsyncImage(
                    model = randomFood.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        LottieAnimation(
                            composition,
                            modifier = Modifier.size(100.dp),
                            iterations = Int.MAX_VALUE
                        )
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = randomFood.name,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
        }

    }
}


