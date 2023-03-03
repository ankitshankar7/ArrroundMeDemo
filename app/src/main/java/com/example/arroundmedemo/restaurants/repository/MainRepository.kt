package com.example.arroundmedemo.restaurants.repository

import android.util.Log
import com.example.arroundmedemo.restaurants.database.RestaurantDao
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.restaurants.network.ApiService
import com.example.arroundmedemo.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.roundToInt

class MainRepository @Inject
constructor(
    private val restaurantDao: RestaurantDao,
    private val apiService: ApiService
) : SafeApiRequest() {

    fun fetchAllFavouriteRestaurants(): Flow<List<RestaurantResponseModel>> {
        return restaurantDao.getAllFavRestaurant(true)
    }

    fun getAllNearByRestaurant(latitude:Double,longitude:Double): Flow<DataState<ArrayList<RestaurantResponseModel>>> = flow {
        emit(DataState.Loading)
        try {
            val nearByRestaurantList=ArrayList<RestaurantResponseModel>()
            val nearByRestaurantData = apiRequest {
                apiService.getAllNearbyRestaurant("cruise","$latitude,$longitude","1500","restaurant",AppConstants.API_KEY)
            }
            nearByRestaurantData.results.forEach {
                nearByRestaurantList.add(RestaurantResponseModel(it.place_id,it.name,it.rating,it.vicinity,false, distance = (distance(latitude,longitude,it.geometry.location.lat,it.geometry.location.lng) * 100.0).roundToInt() / 100.0,it.icon,it.geometry.location.lat,it.geometry.location.lng,it.opening_hours?.open_now?:false))
            }
            if (nearByRestaurantList.isNotEmpty()) {
               // saveRestaurantsToDb(nearByRestaurantList)
                emit(DataState.Success(nearByRestaurantList))
            }
            emit(DataState.Success(nearByRestaurantList))
        } catch (e: NoInternetException) {
            emit(DataState.Error(Exception("No Internet connection")))
        } catch (e: ApiException) {
            emit(DataState.Error(Exception("Api Exception")))
        } catch (e: ConnectionTimedOutException) {
            emit(DataState.Error(Exception("Connection timed out")))
        } catch (e: Exception) {
            emit(DataState.Error(Exception(e.localizedMessage)))
        }

    }.flowOn(Dispatchers.IO)

    private suspend fun saveRestaurantsToDb(nearByRestaurantList: ArrayList<RestaurantResponseModel>) {
        restaurantDao.saveAllRestaurant(nearByRestaurantList)
    }

    fun addOrRemoveFavouriteRestaurants(placeID:String, isFavourite:Boolean) {
        restaurantDao.addOrRemoveFavouriteRestaurants(placeID,isFavourite)
    }

}