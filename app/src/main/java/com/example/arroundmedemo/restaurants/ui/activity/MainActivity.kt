package com.example.arroundmedemo.restaurants.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.arroundmedemo.R
import com.example.arroundmedemo.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }



    private fun setUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        // Find reference to bottom navigation view
        navView = findViewById(R.id.bottom_nav_view)
        // Hook your navigation controller to bottom navigation view
        NavigationUI.setupWithNavController(navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nearByRestaurantFragment) {
                binding.tvTitle.text = getString(R.string.nearby_restaurants)
            } else {
                binding.tvTitle.text = getString(R.string.wishlist)
            }
        }
    }



}