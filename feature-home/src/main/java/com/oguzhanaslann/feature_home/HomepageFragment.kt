package com.oguzhanaslann.feature_home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_home.databinding.FragmentHomepageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.fragment_homepage) {

    private val binding by viewBinding(FragmentHomepageBinding::bind)

    private val viewModel: HomepageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.profileInformation.observe(viewLifecycleOwner) { profileShortInfo ->
            binding.greetingCard.isVisible = profileShortInfo != null
            if (profileShortInfo != null) {
                binding.userProfileImage.load(profileShortInfo.profileShortInfo.userPhotoUrl) {
                    error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
                }
                binding.userName.text = profileShortInfo.profileShortInfo.userFullName
                binding.currentDateText.text = profileShortInfo.currentDateText
            }
        }
    }
}
