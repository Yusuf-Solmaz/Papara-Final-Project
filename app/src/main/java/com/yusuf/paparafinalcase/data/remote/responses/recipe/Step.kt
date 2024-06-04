package com.yusuf.paparafinalcase.data.remote.responses.recipe

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<İngredient>,
    val length: Length,
    val number: Int,
    val step: String
)