package com.oguzhanaslann.fittrack.di

import android.content.Context
import android.provider.SyncStateContract
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.oguzhanaslann.common_data.FitTrackMemorySource
import com.oguzhanaslann.common_data.MemorySource
import com.oguzhanaslann.common_data.local.FitTrackDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = {
                appContext.preferencesDataStoreFile("fittrack_preferences")
            }
        )
    }

    @Provides
    @Singleton
    fun provideFitTrackDataStore(
        dataStore: DataStore<Preferences>
    ): FitTrackDataStore = FitTrackDataStore(dataStore)

    @Provides
    @Singleton
    fun provideMemorySource(): MemorySource = FitTrackMemorySource
}
