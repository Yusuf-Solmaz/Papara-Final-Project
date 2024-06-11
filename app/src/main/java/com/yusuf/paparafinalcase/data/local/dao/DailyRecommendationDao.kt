package com.yusuf.paparafinalcase.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusuf.paparafinalcase.data.local.model.DailyRecommendation
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyRecommendationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyRecommendation(dailyRecommendation: DailyRecommendation)

    @Query("SELECT * FROM daily_recommendation LIMIT 1")
    fun getDailyRecommendation(): Flow<DailyRecommendation?>

    @Query("DELETE FROM daily_recommendation WHERE foodId = :foodId")
    suspend fun deleteDailyRecommendation(foodId: Int)

    @Query("DELETE FROM daily_recommendation")
    suspend fun clearDailyRecommendation()
}
