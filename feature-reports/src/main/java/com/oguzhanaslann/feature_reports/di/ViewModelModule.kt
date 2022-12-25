package com.oguzhanaslann.feature_reports.di

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.feature_reports.domain.mapper.ReportMapper
import com.oguzhanaslann.feature_reports.data.ReportsRepository
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
    fun provideReportsRepository(
        fitTrackDatabase: com.oguzhanaslann.common_data.local.room.FitTrackDatabase,
        fitTrackDataStore: com.oguzhanaslann.common_data.local.FitTrackDataStore
    ) = ReportsRepository(
        userDailyPlanDao = fitTrackDatabase.userDailyPlanDao(),
        fitTrackDataStore = fitTrackDataStore,
        reportMapper =  ReportMapper()
    )

}
