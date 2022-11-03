package com.oguzhanaslann.feature_profile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.feature_profile.databinding.ItemGalleryLayoutBinding
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto

class ProgressPhotoAdapter :
    ListAdapter<ProgressPhoto, ProgressPhotoAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemGalleryLayoutBinding.inflate(
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

    inner class Holder(val binding: ItemGalleryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: ProgressPhoto) = binding.run {
            galleryItemDescription.text = currentItem.description
        }

    }

    class DiffCallBack : DiffUtil.ItemCallback<ProgressPhoto>() {
        override fun areItemsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean =
            oldItem == newItem

    }

}
