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
    fun provideProfileUIStateMapper(): Mapper<Profile, ProfileUIState> =
        Mapper {
            ProfileUIState(
                userProfile = it.userProfile,
                progressPhotos = it.progressPhotos,
                weightProgresses = it.weightProgresses,
                favoriteRecipes = it.favoriteRecipes,
                activeWorkoutPlan = it.activeWorkoutPlan,
                oldWorkouts = it.oldWorkouts
            )
        }
}
