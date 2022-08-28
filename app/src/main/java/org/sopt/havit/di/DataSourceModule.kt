package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.local.HavitAuthLocalPreferences
import org.sopt.havit.data.source.local.AuthLocalDataSource
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.data.source.remote.AuthRemoteDataSource
import org.sopt.havit.data.source.remote.AuthRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.SearchRemoteDataSource
import org.sopt.havit.data.source.remote.SearchRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.category.CategoryRemoteDataSource
import org.sopt.havit.data.source.remote.category.CategoryRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.contents.ContentsRemoteDataSource
import org.sopt.havit.data.source.remote.contents.ContentsRemoteDataSourceImpl
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
    fun provideAuthLocalDataSource(havitLocalPreferences: HavitAuthLocalPreferences): AuthLocalDataSource =
        AuthLocalDataSourceImpl(havitLocalPreferences)

    @Provides
    @Singleton
    fun provideContentsRemoteDataSource(api: HavitApi): ContentsRemoteDataSource =
        ContentsRemoteDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideCategoryRemoteDataSource(api: HavitApi): CategoryRemoteDataSource =
        CategoryRemoteDataSourceImpl(api)
}
