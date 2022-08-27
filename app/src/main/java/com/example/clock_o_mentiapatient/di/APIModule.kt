package com.fantasyclash.zeus.di

import com.example.clock_o_mentiapatient.Retrofit.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Singleton
    @Provides
    fun getAPIService(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)

}