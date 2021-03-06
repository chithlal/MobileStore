package com.chithlal.mobilestore.di

import android.content.Context
import com.chithlal.mobilestore.network.StoreApiInterface
import com.chithlal.mobilestore.utils.Constants
import com.chithlal.mobilestore.utils.NetworkCacheUtil
import com.chithlal.mobilestore.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StoreModule {


    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    fun provideOkHttp(@ApplicationContext context: Context, networkUtil: NetworkUtil) = run {
        //cache the response for offline usage
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseurl: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseurl)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit) = retrofit.create(StoreApiInterface::class.java)

    @Provides
    fun getNetworkUtil(@ApplicationContext context: Context) = NetworkUtil(context)

    @Provides
    fun getNetworkCacheUtil(@ApplicationContext context: Context) = NetworkCacheUtil(context)
}