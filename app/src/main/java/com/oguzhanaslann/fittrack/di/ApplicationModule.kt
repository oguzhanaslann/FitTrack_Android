package com.oguzhanaslann.fittrack.di

import android.content.Context
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideFitTrackDatabase(
        @ApplicationContext context: Context
    ): FitTrackDatabase {
        return FitTrackDatabase.getInstance(context)
    }
}
