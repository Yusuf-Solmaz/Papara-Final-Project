package com.yusuf.paparafinalcase.data.local.repository


import com.yusuf.paparafinalcase.data.local.dao.DailyRecommendationDao
import com.yusuf.paparafinalcase.data.local.model.DailyRecommendation
import com.yusuf.paparafinalcase.data.remote.network.ApiService

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DailyRecommendationRepository @Inject constructor(
    private val dailyRecommendationDao: DailyRecommendationDao,
    private val foodApi: ApiService
) {

    suspend fun getDailyRecommendation(): Flow<DailyRecommendation?> {
        val recommendation = dailyRecommendationDao.getDailyRecommendation().first()
        return if (recommendation == null || isOlderThan24Hours(recommendation.timestamp)) {
            val response = foodApi.getRandomRecipes(number = 1)
            val randomRecipe = response.body()?.recipes?.first() ?: return dailyRecommendationDao.getDailyRecommendation()
            val newRecommendation = DailyRecommendation(

                foodId = randomRecipe.id,
                name = randomRecipe.title,
                image = randomRecipe.image,
                timestamp = System.currentTimeMillis()
            )
            dailyRecommendationDao.clearDailyRecommendation()
            dailyRecommendationDao.insertDailyRecommendation(newRecommendation)
            dailyRecommendationDao.getDailyRecommendation()
        } else {
            dailyRecommendationDao.getDailyRecommendation()
        }
    }

    private fun isOlderThan24Hours(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - timestamp > TimeUnit.HOURS.toMillis(24)
    }
}
