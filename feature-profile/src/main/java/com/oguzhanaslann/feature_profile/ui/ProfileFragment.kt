package com.oguzhanaslann.feature_profile.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.oguzhanaslann.commonui.dp
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val progressPhotoAdapter by lazy {
        ProgressPhotoAdapter(
            onAddPhotoClick = ::onAddPhotoClick,
            firstItemPadding = 8.dp
        )
    }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            // todo handle image
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            horizontalLinearLayoutManaged()

            adapter = progressPhotoAdapter

            progressPhotoAdapter.submitList(
                listOf(
                    ProgressPhoto("1", "url", "description"),
                    ProgressPhoto("2", "url", "description"),
                    ProgressPhoto("3", "url", "description"),
                )
            )
        }

    }

    private fun onAddPhotoClick() {
        takePicturePreview.launch(null)
    }
}
