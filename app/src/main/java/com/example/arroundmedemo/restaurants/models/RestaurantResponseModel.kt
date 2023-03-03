package com.example.arroundmedemo.restaurants.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurant")
data class RestaurantResponseModel(
    @PrimaryKey(autoGenerate = false)
    var place_id:String,
    var name:String,
    val rating:Float,
    val address:String,
    var isFavorite:Boolean?=false,
    val distance:Double,
    val avatar:String,
    val lat: Double,
    val lng: Double,
    val open_now: Boolean
)