package com.oguzhanaslann.feature_home

import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview

data class HomepageUIState(
    val profileShortInfo: ProfileShortInfoUIState?,
    val todaysWorkoutOverview : DailyWorkoutOverview?,
) {
    companion object {
        fun initial() = HomepageUIState(
            profileShortInfo = null,
            todaysWorkoutOverview = null,
        )
    }
}
