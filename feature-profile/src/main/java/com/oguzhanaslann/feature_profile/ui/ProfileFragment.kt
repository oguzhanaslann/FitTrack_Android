package com.oguzhanaslann.feature_profile.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val progressPhotoAdapter by lazy { ProgressPhotoAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            horizontalLinearLayoutManaged()

            adapter = progressPhotoAdapter

            progressPhotoAdapter.submitList(
                listOf(
                    ProgressPhoto(1, "url", "description"),
                    ProgressPhoto(2, "url", "description"),
                    ProgressPhoto(3, "url", "description"),
                )
            )
        }
    }

}
