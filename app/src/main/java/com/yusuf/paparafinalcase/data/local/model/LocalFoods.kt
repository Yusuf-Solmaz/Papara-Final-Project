package com.yusuf.paparafinalcase.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class LocalFoods(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    var foodId: Int,
    var title: String,
    var image: String,
    var glutenFree: Boolean,
    var vegetarian: Boolean,
    var veryHealthy: Boolean,
    var preparationMinutes: Int,
    var cookingMinutes: Int,
    var servings: Int,
    var aggregateLikes: Int,
    var instructions: String
)
