package com.oguzhanaslann.domain_profile.domain.model

import com.oguzhanaslann.common.Height
import com.oguzhanaslann.common.Weight

data class UserProfile(
    val id: Int,
    val fullName: String,
    val profilePhotoUrl: String?,
    val weight : Weight,
    val height : Height,
    val age: Int
)
