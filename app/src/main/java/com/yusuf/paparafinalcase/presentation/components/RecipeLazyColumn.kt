package com.yusuf.paparafinalcase.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.paparafinalcase.R


@Composable
fun LazyColumnRecipeItem(image: String?, title: String, onCardClick: () -> Unit) {

    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            
            image?.let { AsyncImage(image = it) }

            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun AsyncImage(image: String){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_food_image_lottie))

    SubcomposeAsyncImage(
        model = image,
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
}