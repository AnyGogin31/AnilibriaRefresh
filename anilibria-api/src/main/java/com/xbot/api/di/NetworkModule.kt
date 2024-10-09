package com.xbot.api.di

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.xbot.api.service.AnilibriaClient
import com.xbot.api.service.AnilibriaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AnilibriaService.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAnilibriaService(retrofit: Retrofit): AnilibriaService {
        return retrofit.create(AnilibriaService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnilibriaClient(service: AnilibriaService): AnilibriaClient {
        return AnilibriaClient(service)
    }
}