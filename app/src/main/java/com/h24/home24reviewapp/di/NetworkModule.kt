package com.h24.home24reviewapp.di

import com.h24.home24reviewapp.api.Home24Service
import com.h24.home24reviewapp.util.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Module which provides all required dependencies about network
 */
@Module
@Suppress("unused")
class NetworkModule {
    /**
     * Provides the Home24service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return Home24service service implementation.
     */
    @Provides
    @Singleton
    fun provideHome24Service(retrofit: Retrofit): Home24Service {
        return retrofit.create(Home24Service::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    var request = chain.request()
                    // Add constant params using okhttp interceptor
                    val url = request.url().newBuilder().addQueryParameter("appDomain", "1")
                            .addQueryParameter("locale", "de_DE").build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }

        return client.build()
    }
}