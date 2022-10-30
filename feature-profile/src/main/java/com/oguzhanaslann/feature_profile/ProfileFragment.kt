package com.oguzhanaslann.feature_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.databinding.ItemGalleryLayoutBinding

data class ProgressPhoto(val id: Int, val url: String, val description: String)

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

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            horizontalLinearLayoutManaged()
            val _adapter = ProgressPhotoAdapter().apply {

            }

            adapter = _adapter

            _adapter.submitList(
                listOf(
                    ProgressPhoto(1, "url", "description"),
                    ProgressPhoto(2, "url", "description"),
                    ProgressPhoto(3, "url", "description"),
                )
            )
        }
    }

}
