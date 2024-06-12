package com.yusuf.paparafinalcase.data.local.repository

import android.content.Context
import com.yusuf.paparafinalcase.core.constants.utils.notification.NotificationHelper.showNotification
import com.yusuf.paparafinalcase.data.local.dao.DailyRecommendationDao
import com.yusuf.paparafinalcase.data.local.model.DailyRecommendation
import com.yusuf.paparafinalcase.data.remote.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DailyRecommendationRepository @Inject constructor(
    private val context: Context,
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
            showNotification(context,"Daily Recommendation",
                "Check out today's recommended food: ${newRecommendation.name}")
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
