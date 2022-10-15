package com.oguzhanaslann.feature_auth.ui.signin

import com.oguzhanaslann.common.State
import com.oguzhanaslann.feature_auth.domain.SignInResult

data class SignInUIState(
    val email: String,
    val password: String,
    val canSignIn: Boolean,
    val signInState: State<Unit>
) {
    companion object {
        fun initial() = SignInUIState(
            email = "",
            password = "",
            canSignIn = false,
            signInState = State.Initial
        )
    }
}
