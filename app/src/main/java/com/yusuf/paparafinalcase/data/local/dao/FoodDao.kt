package com.yusuf.paparafinalcase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: LocalFoods)

    @Query("SELECT * FROM foods")
    fun getFoods(): Flow<List<LocalFoods>>

    @Query("SELECT COUNT(*) FROM foods WHERE foodId = :foodId")
    suspend fun isFoodInFavorites(foodId: Int): Int

    @Query("DELETE FROM foods WHERE foodId = :foodId")
    suspend fun deleteFood(foodId: Int)
}