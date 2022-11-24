package com.oguzhanaslann.feature_profile.data.local

import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.dao.UserDao
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfile
import kotlinx.coroutines.flow.first

interface ProfileLocalDataSource {
    suspend fun getUserId(): Int
    suspend fun getUserProfile(userId: Int): UserProfile?
}

fun ProfileLocalDataSource(
    userDao: UserDao,
    fitTrackDataStore: FitTrackDataStore
) : ProfileLocalDataSource = object : ProfileLocalDataSource {
    override suspend fun getUserId(): Int {
        return fitTrackDataStore.getUserId().first()
    }

    override suspend fun getUserProfile(userId: Int): UserProfile? {
        return userDao.getUserProfile(userId)
    }
}
