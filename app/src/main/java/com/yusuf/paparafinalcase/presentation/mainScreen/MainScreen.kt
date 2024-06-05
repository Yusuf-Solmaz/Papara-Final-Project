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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.core.constants.Constants.categories
import com.yusuf.paparafinalcase.core.constants.Constants.categoryImages
import com.yusuf.paparafinalcase.presentation.components.LazyColumnRecipeItem
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.MainScreenViewModel
import com.yusuf.paparafinalcase.presentation.mainScreen.viewmodel.MainSearchRecipeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val mainSearchRecipeState by viewModel.rootMainSearchRecipeResponse.collectAsState(MainSearchRecipeState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Food App") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValues ->
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
                    Recommendations()
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
        placeholder = { Text(text = "Search your recipes") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
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
            }
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
                    onClick = { /* TODO: Handle click */ },
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
                    navController = navController,
                    onClick = { categoryName ->
                        navController.navigate("food_screen/$categoryName")
                    }
                )
            }
        }
    }
}


@Composable
fun CategoryItem(category: String, navController: NavController, onClick: (String) -> Unit) {
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
fun Recommendations() {
    Column {
        Text(text = "Recommendation for vegan", style = MaterialTheme.typography.displaySmall)
        // Dummy data for recommendations
        val recommendations = listOf(
            "Ginger roasted tomato",
            "Rice pudding topped with cinnamon"
        )
        recommendations.forEach { title ->
            RecommendationItem(title = title)
        }
    }
}

@Composable
fun RecommendationItem(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = "by Chef Name", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

