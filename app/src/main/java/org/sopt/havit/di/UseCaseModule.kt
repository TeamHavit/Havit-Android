package org.sopt.havit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.repository.SearchRepositoryImpl
import org.sopt.havit.data.repository.UrlRepositoryImpl
import org.sopt.havit.domain.usecase.LoadOgDataUseCase
import org.sopt.havit.domain.usecase.SearchUseCase
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
        LoadOgDataUseCase(urlRepositoryImpl)
}
