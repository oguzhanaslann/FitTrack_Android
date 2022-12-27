package com.oguzhanaslann.feature_home.data

import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview
import kotlinx.coroutines.flow.Flow

interface HomepageRepository {
    suspend fun getTodaysWorkoutOverview(): Flow<DailyWorkoutOverview?>
}
