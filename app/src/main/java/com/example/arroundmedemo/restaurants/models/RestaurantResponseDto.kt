package com.example.arroundmedemo.restaurants.models

data class RestaurantResponseDto(
    val results: List<Result>,
)

data class Geometry(
    val location: Location,
)
data class Location(
    val lat: Double,
    val lng: Double
)

data class OpeningHours(
    val open_now: Boolean?=false
)
data class Result(
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val opening_hours: OpeningHours?,
    val place_id: String,
    val price_level: Int,
    val rating: Float,
    val vicinity: String
)