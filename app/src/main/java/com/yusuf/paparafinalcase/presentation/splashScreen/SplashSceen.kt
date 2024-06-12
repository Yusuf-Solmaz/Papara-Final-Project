package com.yusuf.paparafinalcase.presentation.splashScreen


import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.paparafinalcase.R
import com.yusuf.paparafinalcase.core.constants.Constants.APP_NAME
import com.yusuf.paparafinalcase.ui.theme.Orange
import com.yusuf.paparafinalcase.ui.theme.White


@Composable
fun SplashScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 2000), repeatMode = RepeatMode.Restart),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Column(Modifier.fillMaxSize().background(Orange), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_splash),
            contentDescription = null,
            modifier = Modifier
                .size(170.dp)
                .graphicsLayer(rotationZ = rotation)

        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = APP_NAME, style = TextStyle(
            fontFamily = FontFamily(Font(R.font.splash_title_font)),
            fontSize = 50.sp,
            color = White
        ))
    }

}

