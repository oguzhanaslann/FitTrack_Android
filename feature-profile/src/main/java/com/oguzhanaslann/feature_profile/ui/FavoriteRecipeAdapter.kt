package com.oguzhanaslann.feature_profile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.feature_profile.databinding.FavoriteRecipesLayoutBinding

class FavoriteRecipeAdapter(

) : ListAdapter<FavoriteRecipe, FavoriteRecipeAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FavoriteRecipesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class Holder(val binding: FavoriteRecipesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: FavoriteRecipe) = binding.run {
            favoriteRecipeImage.load(currentItem.imageUrl)
            favoriteRecipeTitle.text = currentItem.name
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<FavoriteRecipe>() {
        override fun areItemsTheSame(
            oldItem: FavoriteRecipe,
            newItem: FavoriteRecipe
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FavoriteRecipe,
            newItem: FavoriteRecipe
        ): Boolean = oldItem == newItem
    }

}
