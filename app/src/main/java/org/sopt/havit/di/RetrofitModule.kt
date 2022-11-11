package org.sopt.havit.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.havit.BuildConfig
import org.sopt.havit.BuildConfig.DEBUG
import org.sopt.havit.BuildConfig.HAVIT_BASE_URL_RELEASE
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideInterceptor(authLocalDataSourceImpl: AuthLocalDataSourceImpl) =
        Interceptor { chain ->
            with(chain) {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "x-auth-token",
                            authLocalDataSourceImpl.getAccessToken()
                        )
                        .build()
                )
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpInterceptor(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(if (DEBUG) BuildConfig.HAVIT_BASE_URL_DEBUG else HAVIT_BASE_URL_RELEASE)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
