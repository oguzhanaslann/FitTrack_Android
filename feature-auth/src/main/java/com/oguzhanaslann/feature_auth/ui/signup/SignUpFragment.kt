package com.oguzhanaslann.feature_auth.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.onError
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.launchOnViewLifecycleOwnerScope
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_auth.R
import com.oguzhanaslann.feature_auth.databinding.FragmentSignUpBinding
import com.oguzhanaslann.feature_auth.domain.Authenticator
import com.oguzhanaslann.feature_auth.ui.signin.SignInViewModel
import com.oguzhanaslann.feature_auth.util.ALREADY_TAKEN_EMAIL
import com.oguzhanaslann.feature_auth.util.EMAIL_OR_PASSWORD_WRONG
import com.oguzhanaslann.feature_auth.util.FIVE_SECS_MILLIS
import com.oguzhanaslann.feature_auth.util.isEmailValid
import com.oguzhanaslann.feature_auth.util.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val canSignUp : Boolean = false,
    val signUpState: State<Unit> = State.Initial
) {
    companion object {
        fun initial() = SignUpUiState()
    }
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticator: Authenticator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val emailInput = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    private val passwordInput = savedStateHandle.getStateFlow(PASSWORD_KEY, "")
    private val signUpState = MutableStateFlow<State<Unit>>(State.Initial)

    val uiState = combine(
        emailInput,
        passwordInput,
        signUpState
    ) { email, password, signUpState ->
        SignUpUiState(
            email = email,
            password = password,
            canSignUp = email.isNotEmpty() && password.isNotEmpty(),
            signUpState = signUpState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(FIVE_SECS_MILLIS),
        SignUpUiState.initial()
    )

    fun setUserEmail(email: String) {
        savedStateHandle[EMAIL_KEY] = email
    }

    fun setUserPassword(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }

    fun signUp() {
        viewModelScope.launch {
            signUpState.value = State.Loading
            val isEmailValid = isEmailValid(emailInput.value)
            val isPasswordValid = isPasswordValid(passwordInput.value)
            val isValid = isEmailValid && isPasswordValid

            val state = when {
                isValid -> signUpUser(emailInput.value, passwordInput.value)
                !isEmailValid -> State.Error(SignInViewModel.EMAIL_INVALID)
                else -> State.Error(SignInViewModel.PASSWORD_INVALID)
            }

            signUpState.value = state
        }
    }

    private suspend fun signUpUser(email: String, password: String): State<Unit> {
        val result: Result<Unit> = authenticator.signUp(email, password)
        return result.toState()
    }

    fun setSignInStateAsConsumed() {
        signUpState.update { State.Initial }
    }

    companion object {
        private const val EMAIL_KEY = "email"
        private const val PASSWORD_KEY = "password"
    }

}

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding { FragmentSignUpBinding.bind(it) }
    private val signUpViewModel: SignUpViewModel by viewModels()

    private val navigator = Navigator()

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
                        navigator.navigateToHome(
                            navController,
                            NavOptions.Builder().setPopUpTo(R.id.nav_graph_auth, true).build()
                        )
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
            SignInViewModel.EMAIL_INVALID -> getString(R.string.email_invalid)
            SignInViewModel.PASSWORD_INVALID -> getString(R.string.password_invalid)
            ALREADY_TAKEN_EMAIL -> getString(R.string.already_taken_email)
            else -> getString(R.string.unknown_error)
        }
    }
}
