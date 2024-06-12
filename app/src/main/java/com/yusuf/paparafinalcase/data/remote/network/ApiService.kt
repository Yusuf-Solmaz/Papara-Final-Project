package com.yusuf.paparafinalcase.data.remote.network

import com.yusuf.paparafinalcase.BuildConfig
import com.yusuf.paparafinalcase.data.remote.responses.recipe.RandomRecipeRoot
import com.yusuf.paparafinalcase.data.remote.responses.recipe.RecipeInfoRoot
import com.yusuf.paparafinalcase.data.remote.responses.searchRecipe.SearchRecipeRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String= BuildConfig.API_KEY,
        @Query("number") number: Int = 10
    ): Response<RandomRecipeRoot>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String= BuildConfig.API_KEY
    ): Response<RecipeInfoRoot>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String= BuildConfig.API_KEY,
        @Query("query") query: String?,
        @Query("number") number: Int = 10,
        @Query("diet") diet: String?,
        @Query("cuisine") cuisine: String?
    ): Response<SearchRecipeRoot>


}