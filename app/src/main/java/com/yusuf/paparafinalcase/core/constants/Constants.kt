package com.yusuf.paparafinalcase.core.constants

import com.yusuf.paparafinalcase.R

object Constants {
    val categories = listOf(
"African", "Asian", "American", "Caribbean", "Chinese", "European", "French", "German", "Indian", "Japanese", "Mexican")

    val categoryImages = mapOf(
        "African" to R.drawable.african,
        "Asian" to R.drawable.asian,
        "American" to R.drawable.american,
        "Caribbean" to R.drawable.caribbean,
        "Chinese" to R.drawable.chinese,
        "European" to R.drawable.european,
        "French" to R.drawable.french,
        "German" to R.drawable.german,
        "Indian" to R.drawable.indian,
        "Japanese" to R.drawable.japanese,
        "Mexican" to R.drawable.mexican
    )

    const val APP_NAME = "Sweet Whispers"
    const val SEARCH_SCREEN_TITLE = "Search"
    const val FAVORITE_SCREEN_TITLE = "Favorites"

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
}