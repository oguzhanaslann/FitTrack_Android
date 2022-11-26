package com.oguzhanaslann.feature_profile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.ItemAddGalleryLayoutBinding
import com.oguzhanaslann.feature_profile.databinding.ItemGalleryLayoutBinding
import com.oguzhanaslann.feature_profile.domain.model.ProgressPhoto

class ProgressPhotoAdapter(
    private inline val onAddPhotoClick: () -> Unit = {},
    private inline val onPhotoClick: (ProgressPhoto) -> Unit = {},
    private inline val onFirstItemBound : (View) -> Unit = {}
) : ListAdapter<ProgressPhoto, RecyclerView.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ADD_PHOTO_CARD_TYPE -> {
                val binding = ItemAddGalleryLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                AddPhotoHolder(binding)
            }
            else -> {
                val binding = ItemGalleryLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                Holder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.id) {
            ADD_PHOTO_CARD_ID -> ADD_PHOTO_CARD_TYPE
            else -> PROGRESS_PHOTO_CARD_TYPE
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is Holder -> holder.onBind(getItem(position), position)
            is AddPhotoHolder -> holder.onBind(getItem(position), position)
        }
    }

    inner class Holder(val binding: ItemGalleryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: ProgressPhoto, position: Int) = binding.run {
            addPaddingToFirstItem(position)
            galleryItemDescription.text = currentItem.description
        }

        private fun ItemGalleryLayoutBinding.addPaddingToFirstItem(
            position: Int
        ) {
            if (position == 0) {
               onFirstItemBound(root)
            }
        }
    }


    inner class AddPhotoHolder(val binding: ItemAddGalleryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: ProgressPhoto, position: Int) = binding.run {
            addPaddingToFirstItem(position)
            val context = root.context
            val addPhotoView = context.getString(R.string.add_photo)
            galleryItemDescription.text = addPhotoView
            binding.cardContainerAdd.setOnClickListener { onAddPhotoClick() }
        }

        private fun ItemAddGalleryLayoutBinding.addPaddingToFirstItem(
            position: Int
        ) {
            if (position == 0) {
                onFirstItemBound(root)
            }
        }
    }

    override fun submitList(list: List<ProgressPhoto>?) {
        val newList = mutableListOf<ProgressPhoto>()
        newList.add(ProgressPhoto(ADD_PHOTO_CARD_ID, "", ""))
        newList.addAll(list ?: emptyList())
        super.submitList(newList)
    }


    class DiffCallBack : DiffUtil.ItemCallback<ProgressPhoto>() {
        override fun areItemsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProgressPhoto, newItem: ProgressPhoto): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val ADD_PHOTO_CARD_ID = "sQ2Ot8TukJ"

        const val ADD_PHOTO_CARD_TYPE = 0
        const val PROGRESS_PHOTO_CARD_TYPE = 1
    }



}
