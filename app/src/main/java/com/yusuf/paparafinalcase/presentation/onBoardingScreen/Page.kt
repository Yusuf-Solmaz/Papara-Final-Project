package com.yusuf.paparafinalcase.presentation.onBoardingScreen

import com.yusuf.paparafinalcase.R

data class Page(
    val title: String,
    val description: String,
    val lottieFile: Int
)


val pager = listOf(
    Page(
        "Discover Culinary Wonders",
        "Embark on a delicious journey with FoodApp, your ultimate guide to exploring a world of flavors. Discover recipes, cooking tips, and culinary secrets from every corner of the globe.",
        R.raw.loading_food_image_lottie
    ),
    Page(
        "Savor Every Moment",
        "Transform everyday meals into extraordinary experiences with FoodApp. From quick bites to gourmet dishes, find inspiration and tips to make every meal memorable.",
        R.raw.general_loading_lottie
    ),
    Page(
        "Your Kitchen Companion",
        "Welcome to FoodApp, your trusty companion in the kitchen. Whether you're a seasoned chef or a culinary novice, we've got you covered with recipes, tutorials, and a community of food lovers.",
        R.raw.general_loading_lottie
    )

)
