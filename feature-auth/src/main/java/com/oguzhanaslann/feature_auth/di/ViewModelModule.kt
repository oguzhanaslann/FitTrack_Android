package com.oguzhanaslann.feature_auth.di

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
    fun provideAuthenticator(): Authenticator {
        return AuthenticationRepository()
    }
}
