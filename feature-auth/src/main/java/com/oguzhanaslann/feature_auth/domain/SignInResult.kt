package com.oguzhanaslann.feature_auth.domain

data class SignInResult(
    val isSuccess: Boolean,
    val error: String?
)
