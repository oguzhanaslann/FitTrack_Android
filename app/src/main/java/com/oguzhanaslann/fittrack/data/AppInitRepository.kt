package com.oguzhanaslann.fittrack.data

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import kotlinx.coroutines.flow.first

interface AppInitRepository {
    suspend fun hasSeenOnboarding(): Boolean
    suspend fun hasAuthenticated(): Boolean
    suspend fun isDatabasePopulated(): Boolean
    suspend fun setDatabasePopulated()
}

fun AppInitRepository(
    fitTrackDataStore: FitTrackDataStore,
): AppInitRepository {
    return object : AppInitRepository {
        override suspend fun hasSeenOnboarding(): Boolean {
            return fitTrackDataStore.getUserSeenOnboarding().first()
        }

        override suspend fun hasAuthenticated(): Boolean {
            return fitTrackDataStore.getUserLoggedIn().first()
        }

        override suspend fun isDatabasePopulated(): Boolean {
            return fitTrackDataStore.getDatabasePopulated().first()
        }

        override suspend fun setDatabasePopulated() {
            fitTrackDataStore.setDatabasePopulated(true)
        }
    }
}
