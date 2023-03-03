package com.example.arroundmedemo.restaurants.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.arroundmedemo.R
import com.example.arroundmedemo.databinding.FragmentNearByRestaurantBinding
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.restaurants.ui.adapter.RestaurantListAdapter
import com.example.arroundmedemo.restaurants.viewmodel.MainViewModel
import com.example.arroundmedemo.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearByRestaurantFragment : Fragment(),RestaurantListAdapter.RestaurantListener {
    private var _binding: FragmentNearByRestaurantBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var nearByRestaurantAdapter: RestaurantListAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNearByRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getMyCurrentLocation()
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        nearByRestaurantAdapter = RestaurantListAdapter(this@NearByRestaurantFragment)
        binding.rvWishList.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = nearByRestaurantAdapter
        }
    }


    private fun setupObservers() {
        viewModel.nearByRestaurantList.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    binding.progressBar.showView()
                }
                is DataState.Success -> {
                    binding.progressBar.hideView()
                    showNoDataFound(dataState.data)
                    nearByRestaurantAdapter.setRestaurantList(dataState.data)
                }
                is DataState.Error -> {
                    binding.progressBar.hideView()
                    toast("${dataState.exception.message}")
                }
                else -> {

                }
            }
        }
    }

    private fun showNoDataFound(itemList: List<RestaurantResponseModel>) {
        with(binding) {
            tvNoData.text = getString(R.string.no_nearby_restro)
            if (itemList.isEmpty()) {
                tvNoData.showView()
                rvWishList.hideView()
            } else {
                tvNoData.hideView()
                rvWishList.showView()
            }
        }

    }

    private fun checkForLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionWarningDialog()
            } else {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
            return false
        }
        return true
    }
    private fun permissionWarningDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.location_permission))
        builder.setPositiveButton(getString(R.string.OK)) { _, i ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", requireContext().packageName, null)
            startActivity(intent)
        }
        builder.show()
    }

    private  val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
        val fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        if (fineLocationGranted) {
            findLocation()
        } else {
            permissionWarningDialog()
        }
    }

    @SuppressLint("MissingPermission")
    private fun findLocation() {
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
            viewModel.fetchAllNearByRestaurants(it.latitude,it.longitude)
        }
    }
    private fun getMyCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkForLocationPermissions()
        }else{
            findLocation()
        }
    }

    override fun onRestaurantSelected(data: RestaurantResponseModel) {

    }

    override fun onFavorite(placeId: String,isFavourite:Boolean,position:Int) {
        viewModel.addOrRemoveFavouriteRestaurants(placeId,isFavourite)
        nearByRestaurantAdapter.notifyItemChanged(position)
    }

}