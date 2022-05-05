package org.sopt.havit.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.util.MySharedPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    /*@Provides
    @Singleton
    fun provideSharedPreferencesImpl(@ApplicationContext context: Context) =
        org.sopt.havit.util.MySharedPreference(context)*/
}