package com.yusuf.paparafinalcase.data.mapper

import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import com.yusuf.paparafinalcase.data.remote.responses.recipe.RecipeInfoRoot

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
        instructions = this.instructions
    )
}