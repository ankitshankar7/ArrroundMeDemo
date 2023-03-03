package com.example.arroundmedemo.restaurants.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.arroundmedemo.R
import com.example.arroundmedemo.databinding.ItemMapBinding
import com.example.arroundmedemo.restaurants.models.RestaurantResponseModel
import com.example.arroundmedemo.restaurants.ui.fragments.WishlistFragment
import com.example.arroundmedemo.utils.setSafeOnClickListener

class WishListAdapter(val fragment: WishlistFragment) :
    RecyclerView.Adapter<WishListAdapter.MatchRowViewHolder>() {
    private var favRestaurantList = ArrayList<RestaurantResponseModel>()
    private val restaurantListener: RestaurantListener = fragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchRowViewHolder {
        val binding = DataBindingUtil.inflate<ItemMapBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_map, parent, false
        )
        return MatchRowViewHolder(binding, restaurantListener)
    }

    override fun getItemCount(): Int {
        return favRestaurantList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRestaurantList(listItems: List<RestaurantResponseModel>) {
        this.favRestaurantList = listItems as ArrayList<RestaurantResponseModel>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MatchRowViewHolder, position: Int) {
        holder.bind(favRestaurantList[position])
    }

    class MatchRowViewHolder(
        private val itemBinding: ItemMapBinding,
        private val restaurantListener: RestaurantListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: RestaurantResponseModel) {
            itemBinding.restaurant = data
            itemBinding.tvStatus.text = if (data.open_now) "Open Now" else "Closed"
            itemBinding.ivWishlist.setImageResource(if(data.isFavorite == true){
                R.drawable.ic_favorite_filled
            }else{
                R.drawable.baseline_favorite_border_24
            }
            )
            itemBinding.ivWishlist.setSafeOnClickListener {
                restaurantListener.onFavorite(data.place_id,data.isFavorite?:false,adapterPosition)
                data.isFavorite=!(data.isFavorite?:false)
            }
            itemView.setSafeOnClickListener {
                restaurantListener.onRestaurantSelected(data)
            }
            itemBinding.executePendingBindings()
        }
    }

    interface RestaurantListener {
        fun onRestaurantSelected(data: RestaurantResponseModel)
        fun onFavorite(placeId: String,isFavourite:Boolean,position: Int)
    }

}
