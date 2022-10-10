package com.oguzhanaslann.feature_auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    // binder
    private var binder: FragmentSignInBinding? = null
    private val binding get() = binder!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder = FragmentSignInBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder = null
    }

}
