package com.oguzhanaslann.domain_profile.domain

import android.util.Log
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfileEntity
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.domain_profile.data.local.ProfileLocalDataSource
import com.oguzhanaslann.domain_profile.domain.model.UserProfileEdit
import com.oguzhanaslann.feature_profile.domain.usecase.PhotoUrlAndLastEditDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProfileRepository {
    suspend fun getProfile(): Flow<Result<Profile>>
    suspend fun setUserProfilePhoto(url: String)
    suspend fun updateProgressPhotos(progressPhotoUrls: List<PhotoUrlAndLastEditDate>)
    suspend fun editUserProfile(userProfileEdit: UserProfileEdit, profilePhotoUrl: String?)
}

fun ProfileRepository(
    profileLocalDataSource: ProfileLocalDataSource,
    mapper: Mapper<UserProfileEntity, Profile>,
): ProfileRepository = object : ProfileRepository {
    override suspend fun getProfile(): Flow<Result<Profile>> {
        val userId = profileLocalDataSource.getUserId()
        return profileLocalDataSource.getUserProfile(userId).map { userProfile ->
            val profileUIState = mapper.map(userProfile)
            Result.success(profileUIState)
        }
    }

    override suspend fun setUserProfilePhoto(url: String) {
        val userId = profileLocalDataSource.getUserId()
        val result = profileLocalDataSource.setUserProfilePhoto(userId, url)
        Log.e("TAG", "setUserProfilePhoto: result : ${result.exceptionOrNull()}")
    }

    override suspend fun updateProgressPhotos(progressPhotoUrls: List<PhotoUrlAndLastEditDate>) {
        val userId = profileLocalDataSource.getUserId()
        profileLocalDataSource.updateProgressPhotosOfUser(progressPhotoUrls, userId)
    }

    override suspend fun editUserProfile(
        userProfileEdit: UserProfileEdit,
        profilePhotoUrl: String?,
    ) {
        val userId = profileLocalDataSource.getUserId()
        profileLocalDataSource.editUserProfile(userId, userProfileEdit, profilePhotoUrl)
    }
}
