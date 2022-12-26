package com.oguzhanaslann.feature_home.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.feature_home.domain.model.ProfileShortInfo

class ProfileShortMapper : Mapper<Profile, ProfileShortInfo> {
    override suspend fun map(input: Profile): ProfileShortInfo {
        return ProfileShortInfo(
            userFullName = input.userProfile.fullName,
            userPhotoUrl = input.userProfile.profilePhotoUrl.orEmpty(),
        )
    }
}
