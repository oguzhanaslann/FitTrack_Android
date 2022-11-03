package com.oguzhanaslann.feature_profile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.commonui.dp
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.ItemAddGalleryLayoutBinding
import com.oguzhanaslann.feature_profile.databinding.ItemGalleryLayoutBinding
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto

class ProgressPhotoAdapter(
    private inline val onAddPhotoClick: () -> Unit = {},
    private var firstItemPadding: Int = 16.dp,
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
                val lm = cardContainer.layoutParams as? ConstraintLayout.LayoutParams
                lm?.let {
                    it.marginStart = firstItemPadding
                }
                cardContainer.layoutParams = lm
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
            binding.cardContainer.setOnClickListener { onAddPhotoClick() }
        }

        private fun ItemAddGalleryLayoutBinding.addPaddingToFirstItem(
            position: Int
        ) {
            if (position == 0) {
                val lm = cardContainer.layoutParams as? ConstraintLayout.LayoutParams
                lm?.let {
                    it.marginStart = firstItemPadding
                }
                cardContainer.layoutParams = lm
            }
        }
    }

    override fun submitList(list: List<ProgressPhoto>?) {
        val newList = mutableListOf<ProgressPhoto>()
        newList.add(ProgressPhoto(ADD_PHOTO_CARD_ID, "", ""))
        newList.addAll(list ?: emptyList())
        super.submitList(newList)
    }


    fun updateFirstItemPadding(padding: Int) {
        firstItemPadding = padding
        notifyDataSetChanged()
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
