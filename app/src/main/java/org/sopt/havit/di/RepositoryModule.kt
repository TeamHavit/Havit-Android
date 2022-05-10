package org.sopt.havit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.mapper.ContentsMapper
import org.sopt.havit.data.repository.AuthRepositoryImpl
import org.sopt.havit.data.repository.ContentsRepositoryImpl
import org.sopt.havit.data.repository.MyPageRepositoryImpl
import org.sopt.havit.data.repository.SearchRepositoryImpl
import org.sopt.havit.data.source.remote.AuthRemoteDataSourceImpl
import org.sopt.havit.data.source.remote.SearchRemoteDataSourceImpl
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.domain.repository.MyPageRepository
import org.sopt.havit.domain.repository.SearchRepository
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
        @ApplicationContext context: Context
    ): MyPageRepository = MyPageRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideContentsRepository(
        @ApplicationContext context: Context
    ): ContentsRepository = ContentsRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl,
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthRepository = AuthRepositoryImpl(authRemoteDataSourceImpl,authLocalDataSourceImpl)

}