package com.oguzhanaslann.feature_auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.setSpan
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    // binder
    private var binder: FragmentSignInBinding? = null
    private val binding get() = binder!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder = FragmentSignInBinding.bind(view)
        binding.moveToSignUpText.setSpan(
            getString(R.string.don_t_have_an_account_sign_up),
            getString(R.string.don_t_have_an_account_sign_up_span_text),
            onSpanClicked = {
                navController.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder = null
    }

}
