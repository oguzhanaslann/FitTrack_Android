package com.oguzhanaslann.feature_onboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzhanaslann.commonui.attachTabsWithPager
import com.oguzhanaslann.feature_onboard.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment(R.layout.fragment_onboard) {

    private var _binder: FragmentOnboardBinding? = null
    private val binding get() = _binder!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binder = FragmentOnboardBinding.bind(view)
        binding.onboardViewPager.adapter = OnboardingPagerAdapter(childFragmentManager,lifecycle)
        attachTabsWithPager(binding.onboardTabLayout, binding.onboardViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }
}
