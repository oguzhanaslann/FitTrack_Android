package com.oguzhanaslann.feature_profile.di

import android.content.Context
import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.FitTrackDatabase
import com.oguzhanaslann.feature_profile.data.local.ProfileLocalDataSource
import com.oguzhanaslann.feature_profile.domain.model.ProfileRepository
import com.oguzhanaslann.feature_profile.domain.model.ProgressionPhotoToProgressPhotoMapper
import com.oguzhanaslann.feature_profile.domain.model.RecipeEntityToFavoriteRecipeMapper
import com.oguzhanaslann.feature_profile.domain.model.UserProfileToProfileUIStateMapper
import com.oguzhanaslann.feature_profile.domain.model.UserWorkoutWithDailyPlansToActiveWorkoutPlanMapper
import com.oguzhanaslann.feature_profile.domain.model.UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper
import com.oguzhanaslann.feature_profile.domain.model.WeightRecordToWeightProgressMapper
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(
        profileLocalDataSource: ProfileLocalDataSource
    ): ProfileRepository {
        return ProfileRepository(
            profileLocalDataSource = profileLocalDataSource,
            mapper = UserProfileToProfileUIStateMapper(
                progressionMapper = ProgressionPhotoToProgressPhotoMapper(),
                weightMapper = WeightRecordToWeightProgressMapper(),
                recipeMapper = RecipeEntityToFavoriteRecipeMapper(),
                workoutMapper = UserWorkoutWithDailyPlansToActiveWorkoutPlanMapper(),
                oldWorkoutMapper = UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper()
            )
        )
    }

    @Provides
    @ViewModelScoped
    fun provideProfileLocalDataSource(
        fitTrackDatabase: FitTrackDatabase,
        fitTrackDataStore: FitTrackDataStore
    ): ProfileLocalDataSource {
        return ProfileLocalDataSource(
            userDao = fitTrackDatabase.userDao(),
            progressionPhotoDao = fitTrackDatabase.progressionPhotoDao(),
            fitTrackDataStore = fitTrackDataStore
        )
    }

    @Provides
    @ViewModelScoped
    fun provideLocalPhotosUseCase(
        profileRepository: ProfileRepository,
        @ApplicationContext context: Context
    ): LocalPhotosUseCase {
        return LocalPhotosUseCase(
            profileRepository = profileRepository,
            applicationContext = context
        )
    }
}
