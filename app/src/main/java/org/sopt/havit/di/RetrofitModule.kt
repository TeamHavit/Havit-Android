package org.sopt.havit.di

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.havit.BuildConfig
import org.sopt.havit.data.source.local.AuthLocalDataSourceImpl
import org.sopt.havit.ui.sign.SplashWithSignActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        @ApplicationContext context: Context,
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("x-auth-token", authLocalDataSourceImpl.getAccessToken())
                .build()
            val response = chain.proceed(newRequest)
            if (response.code() == 401) {
                handle401Error(context, authLocalDataSourceImpl)
            }
            response
        }
    }

    private fun handle401Error(context: Context, authLocalDataSourceImpl: AuthLocalDataSourceImpl) {
        authLocalDataSourceImpl.removeHavitAuthToken()
        authLocalDataSourceImpl.saveAccessToken(authLocalDataSourceImpl.getRefreshToken())
        val intent = Intent(context, SplashWithSignActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(if (BuildConfig.IS_DEV) BuildConfig.HAVIT_BASE_URL_DEV else BuildConfig.HAVIT_BASE_URL_PROD)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
