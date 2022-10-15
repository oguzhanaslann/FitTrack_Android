package com.oguzhanaslann.feature_auth.di

import com.oguzhanaslann.common.data.local.FitTrackDataStore
import com.oguzhanaslann.common.data.local.room.FitTrackDatabase
import com.oguzhanaslann.feature_auth.domain.AuthenticationLocalSource
import com.oguzhanaslann.feature_auth.domain.AuthenticationLocalSourceImpl
import com.oguzhanaslann.feature_auth.domain.AuthenticationRepository
import com.oguzhanaslann.feature_auth.domain.Authenticator
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
    fun provideAuthenticationLocalSource(
        fitTrackDatabase: FitTrackDatabase,
        fitTrackDataStore: FitTrackDataStore
    ): AuthenticationLocalSource {
        return AuthenticationLocalSourceImpl(
            userDao = fitTrackDatabase.userDao(),
            fitTrackDataStore = fitTrackDataStore
        )
    }

    @Provides
    @ViewModelScoped
    fun provideAuthenticator(
        authenticationLocalSource: AuthenticationLocalSource
    ): Authenticator {
        return AuthenticationRepository(authenticationLocalSource)
    }
}
