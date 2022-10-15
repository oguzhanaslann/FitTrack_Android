package com.oguzhanaslann.feature_auth.ui.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_auth.domain.Authenticator
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
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authenticationRepository: Authenticator
) : ViewModel() {

    private val userEmail = savedStateHandle.getStateFlow(USER_EMAIL_KEY, "")
    private val userPassword = savedStateHandle.getStateFlow(USER_PASSWORD_KEY, "")
    private val signInState = MutableStateFlow<State<Unit>>(State.Initial)

    val uiState = combine(
        userEmail,
        userPassword,
        signInState
    ) { email, password, signInState ->
        SignInUIState(
            email = email,
            password = password,
            canSignIn = email.isNotEmpty() && password.isNotEmpty(),
            signInState = signInState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(FIVE_SECS_MILLIS),
        SignInUIState.initial
    )

    fun setUserEmail(email: String) {
        savedStateHandle[USER_EMAIL_KEY] = email
        signInState.update { State.Initial }
    }

    fun setUserPassword(password: String) {
        savedStateHandle[USER_PASSWORD_KEY] = password
        signInState.update { State.Initial }
    }

    fun signIn() {
        viewModelScope.launch {
            signInState.value = State.Loading
            val isEmailValid = isEmailValid(userEmail.value)
            val isPasswordValid = isPasswordValid(userPassword.value)
            val isValid = isEmailValid && isPasswordValid(userPassword.value)

            val state = when {
                isValid -> signInUser(userEmail.value, userPassword.value)
                !isEmailValid -> State.Error(EMAIL_INVALID)
                !isPasswordValid -> State.Error(PASSWORD_INVALID)
                else -> State.Error(UNKNOWN_ERROR)
            }

            signInState.value = state
        }
    }

    private suspend fun signInUser(email: String, password: String): State<Unit> {
        val signInResult = authenticationRepository.signIn(email, password)
        return signInResult.toState()
    }

    fun setSignInStateAsConsumed() {
        signInState.update { State.Initial }
    }

    companion object {
        // user email key
        const val USER_EMAIL_KEY = "userEmail"

        // user password key
        const val USER_PASSWORD_KEY = "userPassword"

        // 5 secs millis
        const val FIVE_SECS_MILLIS = 5000L

        // email invalid
        const val EMAIL_INVALID = "Email is not valid"

        // password invalid
        const val PASSWORD_INVALID = "Password is not valid"

        // unknown error
        const val UNKNOWN_ERROR = "Unknown error"
    }
}
