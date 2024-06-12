package com.yusuf.paparafinalcase.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.ui.theme.Orange
import com.yusuf.paparafinalcase.ui.theme.OrangePass


@Composable
fun LazyColumnRecipeItem(image: String?, title: String, onCardClick: () -> Unit) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Orange, OrangePass)
    )

    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gradient)){

        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
            ){
                image?.let { AsyncImage(image = it,modifier = Modifier.fillMaxSize()) }
            }

            Box(modifier = Modifier.weight(3f)){
                Text(
                    textAlign = TextAlign.Start,
                    text = title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding( 8.dp)
                )
            }


        }
    }
}
}

@Composable
fun AsyncImage(image: String, modifier: Modifier){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_food_image_lottie))

    SubcomposeAsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        loading = {
            LottieAnimation(
                composition,
                modifier = Modifier.size(100.dp),
                iterations = Int.MAX_VALUE
            )
        }
    )
}