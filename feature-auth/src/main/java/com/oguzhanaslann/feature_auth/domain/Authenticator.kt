package com.oguzhanaslann.feature_auth.domain

import com.oguzhanaslann.common.data.local.FitTrackDataStore
import com.oguzhanaslann.common.data.local.room.dao.UserDao
import com.oguzhanaslann.common.data.local.room.entity.UserEntity
import com.oguzhanaslann.feature_auth.util.ALREADY_TAKEN_EMAIL
import com.oguzhanaslann.feature_auth.util.EMAIL_OR_PASSWORD_WRONG
import javax.inject.Inject

interface Authenticator {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
}

interface AuthenticationLocalSource {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
}

class AuthenticationLocalSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val fitTrackDataStore: FitTrackDataStore
) : AuthenticationLocalSource {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val userEntity : UserEntity? = userDao.getUserByEmail(email)
        return if (userEntity != null && userEntity.password == password) {
            fitTrackDataStore.setUserLoggedIn(true)
            Result.success(Unit)
        } else {
            Result.failure(Exception(EMAIL_OR_PASSWORD_WRONG))
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        val userEntity = userDao.getUserByEmail(email)
        val isThereAnyUser = userEntity != null
        return if (isThereAnyUser) {
            Result.failure(Exception(ALREADY_TAKEN_EMAIL))
        } else {
//            userDao.clear() todo not really need to clear whole table
            userDao.insert(UserEntity(email = email, password = password))
            fitTrackDataStore.setUserLoggedIn(true)
            Result.success(Unit)
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

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        // TODO: Now only local source is implemented. Remote source will be implemented later.
        // TODO : after implementing remote source, this method will be changed.
        val signUpResult = authenticationLocalSource.signUp(email, password)
        return signUpResult
    }
}
