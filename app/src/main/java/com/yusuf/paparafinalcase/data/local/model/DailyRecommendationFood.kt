package com.yusuf.paparafinalcase.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_recommendation")
data class DailyRecommendation(
    @PrimaryKey val id: Int=0,
    val foodId: Int,
    val name: String,
    val image: String?,
    val timestamp: Long
)