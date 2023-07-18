package org.sopt.havit.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.havit.BuildConfig.*
import org.sopt.havit.data.api.HavitApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject {

    private fun getRetrofitBuild(jwt: String) = Retrofit.Builder()
        .baseUrl(if (IS_DEV) HAVIT_BASE_URL_DEV else HAVIT_BASE_URL_PROD)
        .client(getOkhttpClient(jwt))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getOkhttpClient(jwt: String) = OkHttpClient.Builder().apply {
        readTimeout(20, TimeUnit.SECONDS)
        connectTimeout(20, TimeUnit.SECONDS)
        writeTimeout(5, TimeUnit.SECONDS)
        addInterceptor(getTokenInterceptor(jwt))

        if (DEBUG) {
            addInterceptor(getLoggingInterceptor())
        }
    }.build()

    private fun getTokenInterceptor(jwt: String) = Interceptor {
        val request = it.request()
            .newBuilder()
            .addHeader("x-auth-token", jwt)
            .build()

        return@Interceptor it.proceed(request)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    fun provideHavitApi(jwt: String): HavitApi = getRetrofitBuild(jwt).create(HavitApi::class.java)
}
