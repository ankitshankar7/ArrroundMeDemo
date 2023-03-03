package com.example.arroundmedemo.restaurants.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.arroundmedemo.databinding.FragmentWishlistBinding
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.restaurants.ui.adapter.RestaurantListAdapter
import com.example.arroundmedemo.restaurants.ui.adapter.WishListAdapter
import com.example.arroundmedemo.restaurants.viewmodel.MainViewModel
import com.example.arroundmedemo.utils.hideView
import com.example.arroundmedemo.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishlistFragment : Fragment(), WishListAdapter.RestaurantListener {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var wishListRestaurantAdapter: WishListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        wishListRestaurantAdapter = WishListAdapter(this)
        binding.rvWishList.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = wishListRestaurantAdapter
        }
    }


    private fun setupObservers() {
        viewModel.fetchAllFavouriteRestaurants()
        lifecycleScope.launch {
            viewModel.favRestaurantList.observe(viewLifecycleOwner) {
                showNoDataFound(it)
                wishListRestaurantAdapter.setRestaurantList(it)
            }
        }
    }

    private fun showNoDataFound(itemList: List<RestaurantResponseModel>) {
        with(binding) {
            if (itemList.isEmpty()) {
                tvNoData.showView()
                rvWishList.hideView()
            } else {
                tvNoData.hideView()
                rvWishList.showView()
            }
        }

    }

    override fun onRestaurantSelected(data: RestaurantResponseModel) {

    }

    override fun onFavorite(placeId: String,isFavourite:Boolean,position:Int) {
        viewModel.addOrRemoveFavouriteRestaurants(placeId,isFavourite)
        wishListRestaurantAdapter.notifyItemChanged(position)
    }


}