package com.oguzhanaslann.feature_auth.domain

import com.oguzhanaslann.common.data.local.FitTrackDataStore
import com.oguzhanaslann.common.data.local.room.FitTrackDatabase
import com.oguzhanaslann.common.data.local.room.dao.UserDao
import com.oguzhanaslann.common.data.local.room.entity.UserEntity
import com.oguzhanaslann.feature_auth.util.EmailOrPasswordWrong
import javax.inject.Inject

interface Authenticator {
    suspend fun signIn(email: String, password: String): Result<Unit>
}

interface AuthenticationLocalSource {
    suspend fun signIn(email: String, password: String): Result<Unit>
}

class AuthenticationLocalSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val fitTrackDataStore: FitTrackDataStore
) : AuthenticationLocalSource {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val userDao =userDao
        val userEntity : UserEntity? = userDao.getUserByEmail(email)
        return if (userEntity != null && userEntity.password == password) {
            fitTrackDataStore.setUserLoggedIn(true)
            Result.success(Unit)
        } else {
            Result.failure(Exception(EmailOrPasswordWrong))
        }
    }
}

class AuthenticationRepository(
    private val authenticationLocalSource: AuthenticationLocalSource
) : Authenticator {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        // TODO: Now only local source is implemented. Remote source will be implemented later.
        // TODO : after implementing remote source, this method will be changed.
        return authenticationLocalSource.signIn(email, password)
    }
}
