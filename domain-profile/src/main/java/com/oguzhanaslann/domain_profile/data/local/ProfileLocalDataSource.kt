package com.oguzhanaslann.domain_profile.data.local

import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.MeasurementUnit
import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.dao.ProgressionPhotoDao
import com.oguzhanaslann.common_data.local.room.dao.UserDao
import com.oguzhanaslann.common_data.local.room.dao.WeightRecordDao
import com.oguzhanaslann.common_data.local.room.entity.UserEntity
import com.oguzhanaslann.common_data.local.room.entity.UserProfileEntity

import com.oguzhanaslann.domain_profile.domain.model.UserProfileEdit
import com.oguzhanaslann.domain_profile.domain.usecase.PhotoUrlAndLastEditDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface ProfileLocalDataSource {
    suspend fun getUserId(): Int
    fun getUser(userId: Int): Flow<UserEntity>
    fun getUserProfile(userId: Int): Flow<UserProfileEntity>
    suspend fun setUserProfilePhoto(userId: Int, url: String): Result<Unit>
    suspend fun updateProgressPhotosOfUser(progressPhotoUrls: List<PhotoUrlAndLastEditDate>, userId: Int)
    suspend fun editUserProfile(userId: Int, userProfileEdit: UserProfileEdit, profilePhotoUrl: String?)
    suspend fun logout()
}

fun ProfileLocalDataSource(
    userDao: UserDao,
    weightRecordDao: WeightRecordDao,
    progressionPhotoDao: ProgressionPhotoDao,
    fitTrackDataStore: FitTrackDataStore
): ProfileLocalDataSource = object : ProfileLocalDataSource {
    override suspend fun getUserId(): Int {
        return fitTrackDataStore.getUserId().first()
    }

    override fun getUser(userId: Int): Flow<UserEntity> {
        return userDao.getUserById(userId)
    }

    override fun getUserProfile(userId: Int): Flow<UserProfileEntity> {
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
            com.oguzhanaslann.common_data.local.room.entity.ProgressionPhotoEntity(
                photoUrl = it.first,
                userId = userId,
                date = it.second.time
            )
        }
        progressionPhotoDao.insertAll(*progressPhotos.toTypedArray())
    }

    override suspend fun editUserProfile(
        userId: Int,
        userProfileEdit: UserProfileEdit,
        profilePhotoUrl: String?,
    ) {
        val user =
            userDao.getUserByIdSuspend(userId) ?: return
        val userEntity = user.copy(
            name = userProfileEdit.name,
            surname = userProfileEdit.surname,
            profilePhotoUrl = profilePhotoUrl ?: user.profilePhotoUrl,
            height = userProfileEdit.heightInCm.toDouble(),
            measurementUnit = MeasurementUnit.metric,
            birthdate =  userProfileEdit.birthdate?.time ?: user.birthdate,
        )

        val weight = com.oguzhanaslann.common_data.local.room.entity.WeightRecordEntity(
            weight = userProfileEdit.weightInKg.toDouble(),
            date = DateHelper.nowAsLong(),
            userId = userId
        )

        weightRecordDao.insert(weight)

        userEntity.id = user.id
        userDao.update(userEntity)
    }

    override suspend fun logout() {
        fitTrackDataStore.setUserId(com.oguzhanaslann.common_data.local.FitTrackDataStore.NONE)
        fitTrackDataStore.setUserLoggedIn(false)
    }
}
