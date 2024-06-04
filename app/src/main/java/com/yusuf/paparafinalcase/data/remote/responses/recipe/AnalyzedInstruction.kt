package com.yusuf.paparafinalcase.data.remote.responses.recipe

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)