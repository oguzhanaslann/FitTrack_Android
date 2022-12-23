package com.oguzhanaslann.feature_auth.ui.signup

import com.oguzhanaslann.common.State

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
