package com.yusuf.paparafinalcase.di

import android.content.Context
import com.yusuf.paparafinalcase.data.local.dao.DailyRecommendationDao
import com.yusuf.paparafinalcase.data.local.repository.DailyRecommendationRepository
import com.yusuf.paparafinalcase.data.remote.network.ApiService
import com.yusuf.paparafinalcase.data.remote.repository.getRecipeInformations.GetRecipeInformations
import com.yusuf.paparafinalcase.data.remote.repository.randomRecipeRepo.RandomRecipeRepository
import com.yusuf.paparafinalcase.data.remote.repository.searchRecipeRepository.SearchRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideGetRecipeInformation(apiService: ApiService): GetRecipeInformations {
        return GetRecipeInformations(apiService)
    }

    @Provides
    fun provideDailyRecommendationRepository(@ApplicationContext context: Context, apiService: ApiService, dao: DailyRecommendationDao): DailyRecommendationRepository {
        return DailyRecommendationRepository(context,dao,apiService)
    }


}