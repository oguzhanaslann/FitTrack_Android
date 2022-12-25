package com.oguzhanaslann.feature_auth.data.local

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.dao.UserDao
import com.oguzhanaslann.common_data.local.room.entity.UserEntity
import com.oguzhanaslann.feature_auth.util.ALREADY_TAKEN_EMAIL
import com.oguzhanaslann.feature_auth.util.EMAIL_OR_PASSWORD_WRONG
import javax.inject.Inject

class AuthenticationLocalSourceImpl @Inject constructor(
    private val userDao: com.oguzhanaslann.common_data.local.room.dao.UserDao,
    private val fitTrackDataStore: com.oguzhanaslann.common_data.local.FitTrackDataStore
) : AuthenticationLocalSource {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val userEntity : com.oguzhanaslann.common_data.local.room.entity.UserEntity? = userDao.getUserByEmail(email)
        return if (userEntity != null && userEntity.password == password) {
            userEntity.id?.let { markUserAsLoggedIn(it) }
            Result.success(Unit)
        } else {
            Result.failure(Exception(EMAIL_OR_PASSWORD_WRONG))
        }
    }

    private suspend fun markUserAsLoggedIn(userId: Int) {
        fitTrackDataStore.setUserLoggedIn(true)
        fitTrackDataStore.setUserId(userId)
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        val userEntity = userDao.getUserByEmail(email)
        val isThereAnyUser = userEntity != null
        return if (isThereAnyUser) {
            Result.failure(Exception(ALREADY_TAKEN_EMAIL))
        } else {
//            userDao.clear() todo not really need to clear whole table
            userDao.insert(com.oguzhanaslann.common_data.local.room.entity.UserEntity(email = email,
                password = password))
            userDao.getUserByEmail(email)?.id?.let { markUserAsLoggedIn(it) }
            Result.success(Unit)
        }
    }
}
