package com.oguzhanaslann.feature_auth.domain

interface Authenticator {
    suspend fun signIn(email: String, password: String): Result<Unit>
}

class AuthenticationRepository : Authenticator {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
}
