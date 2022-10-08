package com.oguzhanaslann.feature_onboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.onError
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.attachTabsWithPager
import com.oguzhanaslann.commonui.doOnPageSelected
import com.oguzhanaslann.commonui.isLastPage
import com.oguzhanaslann.commonui.launchOnViewLifecycleOwnerScope
import com.oguzhanaslann.commonui.showErrorPopUpDialog
import com.oguzhanaslann.commonui.showSnackbar
import com.oguzhanaslann.feature_onboard.databinding.FragmentOnboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : Fragment(R.layout.fragment_onboard) {

    private var _binder: FragmentOnboardBinding? = null
    private val binding get() = _binder!!

    private val onboardViewModel: OnboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binder = FragmentOnboardBinding.bind(view)
        binding.onboardViewPager.adapter = OnboardingPagerAdapter(
            binding.root.context,
            childFragmentManager,
            lifecycle
        )

        attachTabsWithPager(binding.onboardTabLayout, binding.onboardViewPager)

        binding.onboardViewPager.doOnPageSelected {
            onboardViewModel.onPageChanged(it)
            if (binding.onboardViewPager.isLastPage) {
                onLastPageReached()
            } else {
                onAnyPageReached()
            }
        }

        launchOnViewLifecycleOwnerScope { lifecycleOwner, scope ->
            onboardViewModel.onboardUIState
                .flowWithLifecycle(lifecycle)
                .collect {
                    binding.onboardViewPager.currentItem = it.currentPage
                    navigateByOnboardState(it.markAsSeenState)
                }
        }
    }

    private fun navigateByOnboardState(markAsSeenState: State<Boolean>) {
        markAsSeenState.onSuccess {
            navigateAuthentication()
        }.onError {
//            context?.showSnackbar(binding.root, getString(R.string.please_try_again))
            showErrorPopUpDialog(
                message = getString(R.string.please_try_again),
                container = binding.root,
                onDialogDismissed = {}
            )
        }
    }

    private fun navigateAuthentication() {
        //TODO("Not yet implemented")
    }

    private fun onLastPageReached() {
        binding.goNextButton.text = getString(R.string.onboard_start)
        binding.goNextButton.setOnClickListener(::onOnboardingFinishedListener)
    }

    private fun onOnboardingFinishedListener(view: View) {
        onboardViewModel.onFinishOnboarding()
    }

    private fun onAnyPageReached() {
        binding.goNextButton.text = getString(R.string.onboard_go_next)
        binding.goNextButton.setOnClickListener(::goNextPageClickListener)
    }

    private fun goNextPageClickListener(view: View) {
        onboardViewModel.onNextPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }
}
