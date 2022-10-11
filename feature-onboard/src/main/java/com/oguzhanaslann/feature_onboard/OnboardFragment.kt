package com.oguzhanaslann.feature_onboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavOptions
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.onError
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.attachTabsWithPager
import com.oguzhanaslann.commonui.doOnPageSelected
import com.oguzhanaslann.commonui.isLastPage
import com.oguzhanaslann.commonui.launchOnViewLifecycleOwnerScope
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showErrorPopUpDialog
import com.oguzhanaslann.feature_onboard.databinding.FragmentOnboardBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val TAG = "OnboardFragment"

@AndroidEntryPoint
class OnboardFragment : Fragment(R.layout.fragment_onboard) {

    private var _binder: FragmentOnboardBinding? = null
    private val binding get() = _binder!!

    private val onboardViewModel: OnboardViewModel by viewModels()

    private val navigator = Navigator()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = onboardViewModel.canGoBack()
            onboardViewModel.goBack()
            Timber.d("OnboardFragment handleOnBackPressed: handled, isEnabled: $isEnabled")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binder = FragmentOnboardBinding.bind(view)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        binding.onboardViewPager.adapter = OnboardingPagerAdapter(
            binding.root.context,
            childFragmentManager,
            lifecycle
        )

        attachTabsWithPager(binding.onboardTabLayout, binding.onboardViewPager)

        binding.onboardViewPager.currentItem = onboardViewModel.getCurrentPage()

        binding.onboardViewPager.doOnPageSelected {
            onboardViewModel.onPageChanged(it)
            if (binding.onboardViewPager.isLastPage) {
                onLastPageReached()
            } else {
                onAnyPageReached()
            }
        }
    }

    private fun subscribeObservers() {
        launchOnViewLifecycleOwnerScope { lifecycleOwner, scope ->
            onboardViewModel.onboardUIState
                .flowWithLifecycle(lifecycle)
                .collect {
                    onBackPressedCallback.isEnabled = onboardViewModel.canGoBack()
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
        navigator.navigateToAuthentication(
            navController = navController,
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph_onboard, true)
                .build()
        )
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
