package com.oguzhanaslann.domain_profile.domain.usecase

import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.domain_profile.domain.model.UserProfileEdit
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase

class ProfileEditUseCase(
    private val profileRepository: ProfileRepository,
    private val localPhotosUseCase: LocalPhotosUseCase,
) {
    suspend operator fun invoke(userProfileEdit: UserProfileEdit) {
        val profilePhotoUrl = userProfileEdit.profilePhotoUri?.let {
            localPhotosUseCase.saveProfilePhoto(it)
        }

        profileRepository.editUserProfile(
            userProfileEdit,
            profilePhotoUrl
        )
    }
}
