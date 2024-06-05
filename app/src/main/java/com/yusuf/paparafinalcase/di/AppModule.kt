package com.yusuf.paparafinalcase.di

import com.yusuf.paparafinalcase.data.remote.network.ApiService
import com.yusuf.paparafinalcase.data.remote.repository.randomRecipeRepo.RandomRecipeRepository
import com.yusuf.paparafinalcase.data.remote.repository.searchRecipeRepository.SearchRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRandomRecipeRepository(apiService: ApiService): RandomRecipeRepository {
        return RandomRecipeRepository(apiService)
    }

    @Provides
    fun provideSearchRecipeRepository(apiService: ApiService): SearchRecipeRepository {
        return SearchRecipeRepository(apiService)
    }

}