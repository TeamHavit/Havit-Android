package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.source.SearchRemoteDataSource
import org.sopt.havit.data.source.SearchRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSearchDataSource(api: HavitApi): SearchRemoteDataSource =
        SearchRemoteDataSourceImpl(api)
}