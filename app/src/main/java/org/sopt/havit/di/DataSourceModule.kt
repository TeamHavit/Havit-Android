package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.source.AuthRemoteDataSource
import org.sopt.havit.data.source.AuthRemoteDataSourceImpl
import org.sopt.havit.data.source.SearchRemoteDataSource
import org.sopt.havit.data.source.SearchRemoteDataSourceImpl
import org.sopt.havit.data.source.local.AuthLocalDataSource
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.util.HavitLocalPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSearchDataSource(api: HavitApi): SearchRemoteDataSource =
        SearchRemoteDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(api: HavitApi): AuthRemoteDataSource =
        AuthRemoteDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(havitLocalPreferences: HavitLocalPreferences): AuthLocalDataSource =
        AuthLocalDataSourceImpl(havitLocalPreferences)
}