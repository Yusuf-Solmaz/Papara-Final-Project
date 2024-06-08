package com.yusuf.paparafinalcase.di

import android.content.Context
import androidx.room.Room
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import com.yusuf.paparafinalcase.data.local.db.FoodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesFoodsDao(database: FoodDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    @Singleton
    fun providesFoodDatabase(
        @ApplicationContext context: Context
    ): FoodDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = FoodDatabase::class.java,
            name = "food_database.db"
        ).build()
    }


}