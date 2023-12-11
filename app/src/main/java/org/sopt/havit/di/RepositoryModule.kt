package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.mapper.ContentsMapper
import org.sopt.havit.data.repository.*
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.data.source.remote.AuthRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.RemoteConfigDataSourceImpl
import org.sopt.havit.data.source.remote.SearchRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.category.CategoryRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.contents.ContentsRemoteDataSourceImpl
import org.sopt.havit.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSearchRepository(
        searchRemoteDataSourceImpl: SearchRemoteDataSourceImpl,
        contentsMapper: ContentsMapper,
    ): SearchRepository =
        SearchRepositoryImpl(searchRemoteDataSourceImpl, contentsMapper)

    @Provides
    @Singleton
    fun provideMyPageRepository(
        havitApi: HavitApi,
    ): MyPageRepository = MyPageRepositoryImpl(havitApi)

    @Provides
    @Singleton
    fun provideContentsRepository(
        contentsRemoteDataSourceImpl: ContentsRemoteDataSourceImpl,
        havitApi: HavitApi,
    ): ContentsRepository =
        ContentsRepositoryImpl(contentsRemoteDataSourceImpl, havitApi)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl,
    ): CategoryRepository = CategoryRepositoryImpl(categoryRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl,
        authLocalDataSourceImpl: AuthLocalDataSourceImpl,
    ): AuthRepository = AuthRepositoryImpl(authRemoteDataSourceImpl, authLocalDataSourceImpl)


    @Provides
    @Singleton
    fun provideSystemMaintenanceRepository(
        systemMaintenanceDataSource: RemoteConfigDataSourceImpl,
    ): SystemMaintenanceRepository = SystemMaintenanceRepositoryImpl(systemMaintenanceDataSource)
}
