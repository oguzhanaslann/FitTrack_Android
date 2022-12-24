package com.oguzhanaslann.feature_auth.domain

import com.oguzhanaslann.feature_auth.data.local.AuthenticationLocalSource

interface Authenticator {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
}

class AuthenticationRepository(
    private val authenticationLocalSource: AuthenticationLocalSource
) : Authenticator {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        // TODO: Now only local source is implemented. Remote source will be implemented later.
        // TODO : after implementing remote source, this method will be changed.
        return authenticationLocalSource.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        // TODO: Now only local source is implemented. Remote source will be implemented later.
        // TODO : after implementing remote source, this method will be changed.
        val signUpResult = authenticationLocalSource.signUp(email, password)
        return signUpResult
    }
}
