package com.oguzhanaslann.feature_onboard

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.oguzhanaslann.feature_onboard.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {
    private var _binder: FragmentOnboardingPageBinding? = null
    private val binding get() = _binder!!

    private val title: String
        get() = arguments?.getString(ARG_TITLE) ?: ""

    private val description: String
        get() = arguments?.getString(ARG_DESCRIPTION) ?: ""

    private val imageRes: Int
        get() = arguments?.getInt(ARG_IMAGE_RES) ?: 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binder = FragmentOnboardingPageBinding.bind(view)
        binding.run {
            onboardingPageTitle.text = title
            onboardingPageDescription.text = description
            onboardingPageImage.setImageResource(imageRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }

    companion object {
        const val ARG_IMAGE_RES = "as_re_gr_ie_ma"
        const val ARG_TITLE = "ae_rl_gt_tt_i"
        const val ARG_DESCRIPTION = "ae_rl_gt_de_sc_ri_pt_i"

        @JvmStatic
        fun newInstance(
            pageTitle: String,
            pageDescription: String,
            @DrawableRes pageImage: Int
        ) = OnboardingPageFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, pageTitle)
                putString(ARG_DESCRIPTION, pageDescription)
                putInt(ARG_IMAGE_RES, pageImage)
            }
        }
    }
}
