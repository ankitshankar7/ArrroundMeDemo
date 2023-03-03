package com.example.arroundmedemo.restaurants.database

import androidx.room.*
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRestaurant(nearByRestaurantList: ArrayList<RestaurantResponseModel>)

    @Query("SELECT * FROM Restaurant")
    fun getAllRestaurant(): List<RestaurantResponseModel>

    @Query("SELECT * FROM Restaurant WHERE isFavorite = :isFavorite")
    fun getAllFavRestaurant(isFavorite: Boolean): Flow<List<RestaurantResponseModel>>

    @Query("UPDATE  Restaurant  SET isFavorite = :isFavorite WHERE place_id=:placeId ")
    fun addOrRemoveFavouriteRestaurants(placeId: String, isFavorite: Boolean):Int
}