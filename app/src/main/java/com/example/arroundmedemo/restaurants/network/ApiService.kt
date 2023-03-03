package com.example.arroundmedemo.restaurants.network
import com.example.arroundmedemo.restaurants.models.RestaurantResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("place/nearbysearch/json")
    suspend fun getAllNearbyRestaurant(@Query("keyword") keyword:String,@Query("location") location:String,
                                       @Query("radius") radius:String,@Query("type") type:String,
                                       @Query("key") key:String
    ): Response<RestaurantResponseDto>
}