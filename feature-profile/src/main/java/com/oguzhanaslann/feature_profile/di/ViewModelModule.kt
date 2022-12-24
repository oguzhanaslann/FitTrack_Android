package com.oguzhanaslann.feature_profile.di

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.feature_profile.ui.ProfileUIState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)

object ViewModelModule {

    @Provides
    @ViewModelScoped
    @ProfileUIStateMapper
    fun provideProfileUIStateMapper(): Mapper<Profile, ProfileUIState> = ProfileMapper()
}

class ProfileMapper : Mapper<Profile, ProfileUIState> {
    override suspend fun map(input: Profile): ProfileUIState {
        return ProfileUIState(
            userProfile = input.userProfile,
            progressPhotos = input.progressPhotos,
            weightProgresses = input.weightProgresses,
            favoriteRecipes = input.favoriteRecipes,
            activeWorkoutPlan = input.activeWorkoutPlan,
            oldWorkouts = input.oldWorkouts
        )
    }
}
