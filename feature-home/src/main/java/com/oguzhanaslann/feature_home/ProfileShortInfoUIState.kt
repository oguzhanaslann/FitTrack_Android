package com.oguzhanaslann.feature_home

import com.oguzhanaslann.feature_home.domain.model.ProfileShortInfo

data class ProfileShortInfoUIState(
    val profileShortInfo: ProfileShortInfo,
    val currentDateText: String,
)
