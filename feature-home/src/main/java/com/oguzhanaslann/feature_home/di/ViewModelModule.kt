package com.oguzhanaslann.feature_home.di

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.feature_home.data.HomepageRepository
import com.oguzhanaslann.feature_home.data.HomepageRepositoryImpl
import com.oguzhanaslann.feature_home.data.TraceWorkoutRepository
import com.oguzhanaslann.feature_home.data.TraceWorkoutRepositoryImpl
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
    fun provideHomepageRepository(
        fitTrackDataStore: FitTrackDataStore,
        fitTrackDatabase: FitTrackDatabase
    ): HomepageRepository = HomepageRepositoryImpl(
        userDao = fitTrackDatabase.userDao(),
        workoutPlanDao = fitTrackDatabase.userWorkoutPlanDao(),
        fitTrackDataStore = fitTrackDataStore
    )

    //TraceWorkoutRepository
    @Provides
    @ViewModelScoped
    fun provideTraceWorkoutRepository(
        fitTrackDatabase: FitTrackDatabase
    ): TraceWorkoutRepository = TraceWorkoutRepositoryImpl(
        userDailyPlanDao = fitTrackDatabase.userDailyPlanDao(),
        userExerciseDao = fitTrackDatabase.userExerciseDao()
    )
}
