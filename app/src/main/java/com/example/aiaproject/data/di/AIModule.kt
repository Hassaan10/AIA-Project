package com.example.aiaproject.data.di

import com.example.aiaproject.data.di.AnalyticsModule.provideNetworkService
import com.example.aiaproject.data.repository.AIRepository
import com.example.aiaproject.data.repository.AIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object AIModule {

    @Provides
    fun provideNewsRepository(
    ): AIRepository {
        return AIRepositoryImpl(provideNetworkService())
    }

}