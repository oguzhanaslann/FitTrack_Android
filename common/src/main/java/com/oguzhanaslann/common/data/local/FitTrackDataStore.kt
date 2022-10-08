package com.oguzhanaslann.common.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val preferencesUserIsPremiumKey = booleanPreferencesKey("com.oguzhanaslann.fittrack.user_is_premium")

class FitTrackDataStore(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setUserSeenOnboarding(hasSeen: Boolean) =  runSafeSetOperation {
        dataStore.edit { it[preferencesUserIsPremiumKey] = hasSeen }
    }

    suspend fun getUserSeenOnboarding() = dataStore.safeData.map { it[preferencesUserIsPremiumKey] ?: false }

    private val DataStore<Preferences>.safeData
        get() = data.catch {
            emit(emptyPreferences())
        }

    private suspend fun runSafeSetOperation(block: suspend () -> Unit): Result<Unit> {
        return runCatching {
            block()
        }
    }
}
