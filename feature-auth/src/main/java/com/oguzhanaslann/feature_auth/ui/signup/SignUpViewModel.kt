package com.oguzhanaslann.feature_auth.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_auth.domain.Authenticator
import com.oguzhanaslann.feature_auth.ui.signin.SignInViewModel
import com.oguzhanaslann.feature_auth.util.FIVE_SECS_MILLIS
import com.oguzhanaslann.feature_auth.util.isEmailValid
import com.oguzhanaslann.feature_auth.util.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
