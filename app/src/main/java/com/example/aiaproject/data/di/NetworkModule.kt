package com.example.aiaproject.data.di

import com.example.aiaproject.data.network.NetworkService
import com.example.aiaproject.data.network.RequestHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    fun provideNetworkService(
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl("https://openrouter.ai/api/v1/")
            .client(provideOKHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOKHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(RequestHeaderInterceptor())
            .build()
    }
}