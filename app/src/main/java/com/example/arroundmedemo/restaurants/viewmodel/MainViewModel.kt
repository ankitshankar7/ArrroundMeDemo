package com.example.arroundmedemo.restaurants.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.restaurants.repository.MainRepository
import com.example.arroundmedemo.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _favRestaurantList = MutableLiveData<List<RestaurantResponseModel>>()
    val favRestaurantList: LiveData<List<RestaurantResponseModel>>
        get() = _favRestaurantList

 private val _nearByRestaurantList = MutableLiveData<DataState<List<RestaurantResponseModel>>>()
    val nearByRestaurantList: LiveData<DataState<List<RestaurantResponseModel>>>
        get() = _nearByRestaurantList

     fun fetchAllFavouriteRestaurants() {
        viewModelScope.launch {
            repository.fetchAllFavouriteRestaurants().onEach {
                _favRestaurantList.postValue(it)
            }.launchIn(viewModelScope)
        }
    }

      fun fetchAllNearByRestaurants(latitude:Double,longitude:Double) {
          Log.d("Locaccaa","$latitude,$longitude")
        viewModelScope.launch {
            repository.getAllNearByRestaurant(latitude,longitude)
                .onEach {
                    _nearByRestaurantList.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun addOrRemoveFavouriteRestaurants(placeId:String,isFavourite:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrRemoveFavouriteRestaurants(placeId,!isFavourite)
        }
    }

}