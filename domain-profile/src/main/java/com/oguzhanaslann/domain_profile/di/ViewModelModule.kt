package com.oguzhanaslann.domain_profile.di

import android.content.Context
import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.FitTrackDatabase
import com.oguzhanaslann.domain_profile.data.local.ProfileLocalDataSource
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.domain_profile.domain.mapper.ProgressionPhotoToProgressPhotoMapper
import com.oguzhanaslann.domain_profile.domain.mapper.RecipeEntityToFavoriteRecipeMapper
import com.oguzhanaslann.domain_profile.domain.mapper.UserProfileToProfileUIStateMapper
import com.oguzhanaslann.domain_profile.domain.mapper.UserWorkoutWithDailyPlansToActiveWorkoutPlanMapper
import com.oguzhanaslann.domain_profile.domain.mapper.UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper
import com.oguzhanaslann.domain_profile.domain.mapper.WeightRecordToWeightProgressMapper
import com.oguzhanaslann.domain_profile.domain.usecase.ProfileEditUseCase
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
            fitTrackDataStore = fitTrackDataStore,
            weightRecordDao = fitTrackDatabase.weightRecordDao(),
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

    @Provides
    @ViewModelScoped
    fun provideProfileEditUseCase(
        profileRepository: ProfileRepository,
        localPhotosUseCase: LocalPhotosUseCase
    ): ProfileEditUseCase {
        return ProfileEditUseCase(
            profileRepository = profileRepository,
            localPhotosUseCase = localPhotosUseCase
        )
    }
}
