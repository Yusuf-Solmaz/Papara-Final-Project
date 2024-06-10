package com.yusuf.paparafinalcase.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import com.yusuf.paparafinalcase.data.remote.responses.recipe.ExtendedIngredient
import com.yusuf.paparafinalcase.data.remote.responses.recipe.RecipeInfoRoot

fun List<ExtendedIngredient>.toJsonString(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.toIngredientsList(): List<ExtendedIngredient> {
    val gson = Gson()
    val type = object : TypeToken<List<ExtendedIngredient>>() {}.type
    return gson.fromJson(this, type)
}

fun RecipeInfoRoot.toLocalFoods(): LocalFoods{
    return LocalFoods(
        foodId = this.id,
        title = this.title,
        image = this.image,
        glutenFree = this.glutenFree,
        vegetarian = this.vegetarian,
        veryHealthy = this.veryHealthy,
        preparationMinutes = this.preparationMinutes,
        cookingMinutes = this.cookingMinutes,
        servings = this.servings,
        aggregateLikes = this.aggregateLikes,
        instructions = this.instructions,
        ingredients = this.extendedIngredients.toJsonString()

    )
}

fun LocalFoods.toRecipeInfoRoot(): RecipeInfoRoot {

    return RecipeInfoRoot(
        aggregateLikes = this.aggregateLikes,
        analyzedInstructions = listOf(),
        cheap = false,
        cookingMinutes = this.cookingMinutes,
        creditsText = "",
        cuisines = listOf(),
        dairyFree = false,
        diets = listOf(),
        dishTypes = listOf(),
        extendedIngredients = this.ingredients.toIngredientsList(),
        gaps = "",
        glutenFree = this.glutenFree,
        healthScore = 0,
        id = this.foodId,
        image = this.image,
        imageType = "",
        instructions = this.instructions,
        lowFodmap = false,
        occasions = listOf(),
        originalId = null,
        preparationMinutes = this.preparationMinutes,
        pricePerServing = 0.0,
        readyInMinutes = this.preparationMinutes + this.cookingMinutes,
        servings = this.servings,
        sourceName = "",
        sourceUrl = "",
        spoonacularScore = 0.0,
        spoonacularSourceUrl = "",
        summary = "",
        sustainable = false,
        title = this.title,
        vegan = false,
        vegetarian = this.vegetarian,
        veryHealthy = this.veryHealthy,
        veryPopular = false,
        weightWatcherSmartPoints = 0
    )
}