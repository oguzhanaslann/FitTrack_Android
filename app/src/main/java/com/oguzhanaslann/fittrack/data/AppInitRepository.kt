package com.oguzhanaslann.fittrack.data

import com.oguzhanaslann.common.data.local.FitTrackDataStore
import kotlinx.coroutines.flow.first

interface AppInitRepository {
    suspend fun hasSeenOnboarding(): Boolean
    suspend fun hasAuthenticated(): Boolean
}


fun AppInitRepository(
    fitTrackDataStore: FitTrackDataStore
) : AppInitRepository {
    return object : AppInitRepository {
        override suspend fun hasSeenOnboarding(): Boolean {
            return fitTrackDataStore.getUserSeenOnboarding().first()
        }

        override suspend fun hasAuthenticated(): Boolean {
            return false
        }
    }
}
