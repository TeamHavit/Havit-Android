package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.repository.SearchRepositoryImpl
import org.sopt.havit.data.repository.UrlRepositoryImpl
import org.sopt.havit.domain.usecase.SearchUseCase
import org.sopt.havit.domain.usecase.UrlUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSearchUseCase(searchRepositoryImpl: SearchRepositoryImpl) =
        SearchUseCase(searchRepositoryImpl)

    @Provides
    @Singleton
    fun provideUrlUseCase(urlRepositoryImpl: UrlRepositoryImpl) =
        UrlUseCase(urlRepositoryImpl)
}
