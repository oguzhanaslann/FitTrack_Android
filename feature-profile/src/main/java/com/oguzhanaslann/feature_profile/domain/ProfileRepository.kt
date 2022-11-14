package com.oguzhanaslann.feature_profile.domain

import com.oguzhanaslann.feature_profile.ui.ProfileUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ProfileRepository {
    suspend fun getProfileUIState(): Flow<ProfileUIState>
}

fun ProfileRepository() : ProfileRepository = object : ProfileRepository {
    override suspend fun getProfileUIState(): Flow<ProfileUIState> {
       return flow { /*todo implement here */ }
    }
}
