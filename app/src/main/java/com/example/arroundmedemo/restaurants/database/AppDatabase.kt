package com.example.arroundmedemo.restaurants.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.utils.AppConstants

@Database(entities = [RestaurantResponseModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): RestaurantDao
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getDatabase(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppConstants.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

}