package com.oguzhanaslann.feature_profile.ui

import com.oguzhanaslann.common.Height
import com.oguzhanaslann.common.Weight

data class UserProfile(
    val id: Int,
    val name: String,
    val profilePhotoUrl: String?,
    val weight : Weight,
    val height : Height,
    val age: Int

)
