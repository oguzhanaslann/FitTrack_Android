package com.oguzhanaslann.feature_auth.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavOptions
import com.oguzhanaslann.common.onError
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.launchOnViewLifecycleOwnerScope
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentSignUpBinding
import com.oguzhanaslann.feature_auth.ui.signin.SignInViewModel
import com.oguzhanaslann.feature_auth.util.ALREADY_TAKEN_EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding { FragmentSignUpBinding.bind(it) }
    private val signUpViewModel: SignUpViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.signUpEmailInput.editText?.setText(signUpViewModel.uiState.value.email)
        binding.signUpEmailInput.editText?.doAfterTextChanged {
            signUpViewModel.setUserEmail(it?.toString() ?: "")
        }

        binding.signUpPasswordInput.editText?.setText(signUpViewModel.uiState.value.password)
        binding.signUpPasswordInput.editText?.doAfterTextChanged {
            signUpViewModel.setUserPassword(it?.toString() ?: "")
        }

        binding.signUpButton.setOnClickListener {
            signUpViewModel.signUp()
        }

        launchOnViewLifecycleOwnerScope { lifecycleOwner, scope ->
            signUpViewModel.uiState
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .collect { uiState ->
                    binding.signUpButton.isEnabled = uiState.canSignUp

                    uiState.signUpState.onSuccess {
                        signUpViewModel.setSignInStateAsConsumed()
                        navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToProfileSetUpFragment())
                    }.onError {
                        signUpViewModel.setSignInStateAsConsumed()
                        val errorMessage = getErrorMessageFrom(it)
                        showErrorSnackbar(errorMessage, binding.root)
                    }
                }
        }
    }

    private fun getErrorMessageFrom(error: String): String {
        return when (error) {
            SignInViewModel.EMAIL_INVALID -> getString(com.oguzhanaslann.commonui.R.string.email_invalid)
            SignInViewModel.PASSWORD_INVALID -> getString(com.oguzhanaslann.commonui.R.string.password_invalid)
            ALREADY_TAKEN_EMAIL -> getString(com.oguzhanaslann.commonui.R.string.already_taken_email)
            else -> getString(com.oguzhanaslann.commonui.R.string.unknown_error)
        }
    }
}
