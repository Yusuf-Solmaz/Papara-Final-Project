package com.yusuf.paparafinalcase.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import com.yusuf.paparafinalcase.data.local.model.LocalFoods

@Database(entities = [LocalFoods::class], version = 1, exportSchema = false)
abstract class FoodDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
}