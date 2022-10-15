package com.oguzhanaslann.feature_auth.ui.signin

import android.os.Bundle
import android.util.Log
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
import com.oguzhanaslann.commonui.setSpan
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentSignInBinding
import com.oguzhanaslann.feature_auth.util.EMAIL_OR_PASSWORD_WRONG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    // binder
    private var binder: FragmentSignInBinding? = null
    private val binding get() = binder!!

    private val signInViewModel: SignInViewModel by viewModels()

    private val navigator = Navigator()

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

        binding.emailInputLayout.editText?.setText(signInViewModel.uiState.value.email)
        binding.emailInputLayout.editText?.doAfterTextChanged {
            signInViewModel.setUserEmail(it?.toString() ?: "")
        }

        binding.passwordInputLayout.editText?.setText(signInViewModel.uiState.value.password)
        binding.passwordInputLayout.editText?.doAfterTextChanged {
            signInViewModel.setUserPassword(it?.toString() ?: "")
        }

        binding.signInButton.setOnClickListener {
            signInViewModel.signIn()
        }

        launchOnViewLifecycleOwnerScope { lifecycleOwner, scope ->
            signInViewModel.uiState
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .collect { uiState ->
                    binding.signInButton.isEnabled = uiState.canSignIn
                    uiState.signInState
                        .onSuccess {
                            signInViewModel.setSignInStateAsConsumed()
                            navigator.navigateToHome(
                                navController,
                                NavOptions.Builder().setPopUpTo(R.id.nav_graph_auth, true).build()
                            )
                        }.onError {
                            signInViewModel.setSignInStateAsConsumed()
                            val errorMessage = getErrorMessageFrom(it)
                            showErrorSnackbar(errorMessage, binding.root)
                        }
                }
        }
    }

    private fun getErrorMessageFrom(error: String): String {
        return when (error) {
            SignInViewModel.EMAIL_INVALID -> getString(R.string.email_invalid)
            SignInViewModel.PASSWORD_INVALID -> getString(R.string.password_invalid)
            EMAIL_OR_PASSWORD_WRONG -> getString(R.string.email_or_password_wrong)
            else -> getString(R.string.unknown_error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder = null
    }

}
