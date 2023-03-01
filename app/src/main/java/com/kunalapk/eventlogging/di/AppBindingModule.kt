package com.kunalapk.eventlogging.di

import com.kunalapk.eventlogging.data.api.MatchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val CONNECTION_TIMEOUT_IN_SECONDS = 60.toLong()

@Module
@InstallIn(SingletonComponent::class)
class AppBindingModule {

    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().connectTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://mocki.io/v1/").client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideMatchApi(retrofit: Retrofit): MatchApi {
        return retrofit.create(MatchApi::class.java)
    }
}