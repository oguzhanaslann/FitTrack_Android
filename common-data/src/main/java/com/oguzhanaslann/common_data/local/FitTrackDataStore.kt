package com.oguzhanaslann.common_data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val preferencesUserHasSeenOnboardingKey =
    booleanPreferencesKey("com.oguzhanaslann.fittrack.user_has_seen_onboarding")
val preferencesUserIsLoggedInKey =
    booleanPreferencesKey("com.oguzhanaslann.fittrack.user_is_logged_in")
val preferencesUserIdKey = intPreferencesKey("com.oguzhanaslann.fittrack.user_id")

val preferencesIsDatabasePopulatedKey =
    booleanPreferencesKey("com.oguzhanaslann.fittrack.is_database_populated")

class FitTrackDataStore(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun setUserSeenOnboarding(hasSeen: Boolean) = runSafeSetOperation {
        dataStore.edit { it[preferencesUserHasSeenOnboardingKey] = hasSeen }
    }

    suspend fun getUserSeenOnboarding() =
        dataStore.safeData.map { it[preferencesUserHasSeenOnboardingKey] ?: false }

    suspend fun setUserLoggedIn(isLoggedIn: Boolean) = runSafeSetOperation {
        dataStore.edit { it[preferencesUserIsLoggedInKey] = isLoggedIn }
    }

    suspend fun getUserLoggedIn() =
        dataStore.safeData.map { it[preferencesUserIsLoggedInKey] ?: false }

    private val DataStore<Preferences>.safeData
        get() = data.catch {
            emit(emptyPreferences())
        }

    private suspend fun runSafeSetOperation(block: suspend () -> Unit): Result<Unit> {
        return runCatching {
            block()
        }
    }

    suspend fun setUserId(id: Int) {
        runSafeSetOperation { dataStore.edit { it[preferencesUserIdKey] = id } }
    }

    suspend fun getUserId() = dataStore.safeData.map { it[preferencesUserIdKey] ?: NONE }

    suspend fun setDatabasePopulated(isPopulated: Boolean) = runSafeSetOperation {
        dataStore.edit { it[preferencesIsDatabasePopulatedKey] = isPopulated }
    }

    suspend fun getDatabasePopulated() =
        dataStore.safeData.map { it[preferencesIsDatabasePopulatedKey] ?: false }

    companion object {
        const val NONE = -1
    }
}
