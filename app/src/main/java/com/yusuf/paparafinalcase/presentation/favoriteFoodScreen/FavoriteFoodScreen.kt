package com.yusuf.paparafinalcase.presentation.favoriteFoodScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import com.yusuf.paparafinalcase.presentation.components.AsyncImage
import com.yusuf.paparafinalcase.presentation.components.LoadingLottie
import com.yusuf.paparafinalcase.ui.theme.FavoriteFoodViewDetailBtnBG
import com.yusuf.paparafinalcase.ui.theme.GrayOrange
import com.yusuf.paparafinalcase.ui.theme.Orange
import com.yusuf.paparafinalcase.ui.theme.White

@Composable
fun FavoriteFoodScreen(navController: NavController, viewModel: FavoriteFoodViewModel = hiltViewModel()) {
    val state by viewModel.favoriteFoodsState.collectAsState(FavoriteFoodState())

    Scaffold (
        content = {
            paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when {
                    state.isLoading -> {
                        LoadingLottie(resId = R.raw.general_loading_lottie)
                    }
                    state.error != null -> {
                        Text(text = state.error ?: "An error occurred")
                    }
                    state.favoriteFoods.isEmpty() -> {
                        Text(text = "No favorite foods found")
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(
                                    bottom = paddingValues.calculateBottomPadding(),
                                    top = 70.dp
                                ),
                        ) {
                            items(state.favoriteFoods) { food ->
                                ExpandableCard(
                                    food = food,
                                    onDeleted = { viewModel.deleteFavoriteFood(it.foodId) },
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )


}


@Composable
fun ExpandableCard(
    food: LocalFoods,
    onDeleted: (LocalFoods) -> Unit,
    padding: Dp = 12.dp,
    navController: NavController
    ) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            expandedState = !expandedState
        },
        colors = CardDefaults.cardColors(
            containerColor = Orange,
            contentColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(image = food.image, modifier = Modifier.size(100.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(10f)
                        .padding(start = 20.dp),
                    text = food.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = White,
                        containerColor = Orange
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.2f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        tint = Color.Black,
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FavoriteCard(
                        food = food,
                        onDeleted = onDeleted
                    )
                    
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor  = FavoriteFoodViewDetailBtnBG,
                    ),onClick = {
                        navController.navigate("recipe_detail_page/${food.foodId}")
                    }) {
                        Text(text = "View All Details")
                    }
                }

            }
        }
    }
}

@Composable
fun FavoriteCard(
    food: LocalFoods,
    onDeleted: (LocalFoods) -> Unit,
) {
    var shouldShowItemDeletionDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = GrayOrange,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        border = BorderStroke(2.dp, Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = food.instructions,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify,
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                )
                Icon(
                    tint = Color.Black,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Bottom)
                        .clickable {
                            shouldShowItemDeletionDialog = true
                        }
                )
                if (shouldShowItemDeletionDialog) {
                    FavoriteItemDeletionDialog({
                        shouldShowItemDeletionDialog = it
                    }, {
                        onDeleted(food)
                    })
                }
            }
        }
    }
}


@Composable
fun FavoriteItemDeletionDialog(
    onDismiss: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = "Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss(false)
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss(false) }
            ) {
                Text("No")
            }
        }
    )
}

