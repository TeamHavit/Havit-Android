package org.sopt.havit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.local.HavitAuthLocalPreferences
import org.sopt.havit.ui.sign.KakaoLoginService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideHavitService(retrofit: Retrofit): HavitApi =
        retrofit.create(HavitApi::class.java)

    @Provides
    @Singleton
    fun provideKakaoLoginService(
        @ApplicationContext context: Context,
        preference: HavitAuthLocalPreferences
    ): KakaoLoginService =
        KakaoLoginService(context, preference)
}
