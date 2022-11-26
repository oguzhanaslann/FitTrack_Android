package com.oguzhanaslann.feature_profile.data.local

import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.dao.ProgressionPhotoDao
import com.oguzhanaslann.commonui.data.local.room.dao.UserDao
import com.oguzhanaslann.commonui.data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfile
import com.oguzhanaslann.feature_profile.domain.usecase.PhotoUrlAndLastEditDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface ProfileLocalDataSource {
    suspend fun getUserId(): Int
    fun getUser(userId: Int): Flow<UserEntity>
    fun getUserProfile(userId: Int): Flow<UserProfile>
    suspend fun setUserProfilePhoto(userId: Int, url: String): Result<Unit>
    suspend fun updateProgressPhotosOfUser(progressPhotoUrls: List<PhotoUrlAndLastEditDate>, userId: Int)
}

fun ProfileLocalDataSource(
    userDao: UserDao,
    progressionPhotoDao: ProgressionPhotoDao,
    fitTrackDataStore: FitTrackDataStore
): ProfileLocalDataSource = object : ProfileLocalDataSource {
    override suspend fun getUserId(): Int {
        return fitTrackDataStore.getUserId().first()
    }

    override fun getUser(userId: Int): Flow<UserEntity> {
        return userDao.getUserById(userId)
    }

    override fun getUserProfile(userId: Int): Flow<UserProfile> {
        return userDao.getUserProfile(userId)
    }

    override suspend fun setUserProfilePhoto(userId: Int, url: String): Result<Unit> {
        val user =
            userDao.getUserByIdSuspend(userId) ?: return Result.failure(Exception("User not found"))
        val userEntity = user.copy(profilePhotoUrl = url)
        userEntity.id = user.id
        userDao.update(userEntity)
        return Result.success(Unit)
    }

    override suspend fun updateProgressPhotosOfUser(progressPhotoUrls: List<PhotoUrlAndLastEditDate>, userId: Int) {
        progressionPhotoDao.deleteAllPhotosOfUser(userId)
        val progressPhotos = progressPhotoUrls.map {
            ProgressionPhotoEntity(
                photoUrl = it.first,
                userId = userId,
                date = it.second.time
            )
        }
        progressionPhotoDao.insertAll(*progressPhotos.toTypedArray())
    }
}
