package com.oguzhanaslann.feature_onboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.oguzhanaslann.feature_onboard.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment(R.layout.fragment_onboard) {

    private var _binder: FragmentOnboardBinding? = null
    private val binder get() = _binder!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binder = null
    }
}
