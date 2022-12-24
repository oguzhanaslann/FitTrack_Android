package com.oguzhanaslann.feature_auth.data.local

interface AuthenticationLocalSource {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
}
