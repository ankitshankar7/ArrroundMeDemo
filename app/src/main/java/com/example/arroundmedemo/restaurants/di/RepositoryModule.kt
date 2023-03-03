package com.example.arroundmedemo.restaurants.di

import com.example.arroundmedemo.restaurants.database.RestaurantDao
import com.example.arroundmedemo.restaurants.network.ApiService
import com.example.arroundmedemo.restaurants.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @ActivityRetainedScoped
    fun provideMainRepository(restaurantDao: RestaurantDao,apiService: ApiService): MainRepository =
        MainRepository(restaurantDao,apiService)
}