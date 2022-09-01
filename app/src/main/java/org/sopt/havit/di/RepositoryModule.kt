package org.sopt.havit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.mapper.ContentsMapper
import org.sopt.havit.data.repository.*
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.data.source.remote.AuthRemoteDataSourceImpl
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
        contentsMapper: ContentsMapper
    ): SearchRepository =
        SearchRepositoryImpl(searchRemoteDataSourceImpl, contentsMapper)

    @Provides
    @Singleton
    fun provideMyPageRepository(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): MyPageRepository = MyPageRepositoryImpl(authLocalDataSourceImpl)

    @Provides
    @Singleton
    fun provideContentsRepository(
        @ApplicationContext context: Context,
        contentsRemoteDataSourceImpl: ContentsRemoteDataSourceImpl
    ): ContentsRepository = ContentsRepositoryImpl(context, contentsRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl
    ): CategoryRepository = CategoryRepositoryImpl(categoryRemoteDataSourceImpl)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl,
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthRepository = AuthRepositoryImpl(authRemoteDataSourceImpl, authLocalDataSourceImpl)
}
