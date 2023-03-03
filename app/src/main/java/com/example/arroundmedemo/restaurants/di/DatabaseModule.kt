package com.example.arroundmedemo.restaurants.di

import android.content.Context
import com.example.arroundmedemo.restaurants.database.AppDatabase
import com.example.arroundmedemo.restaurants.database.RestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providesRestaurantDao(appDatabase:AppDatabase ):RestaurantDao{
        return appDatabase.getUserDao()
    }

}