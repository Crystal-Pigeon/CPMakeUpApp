package com.crystalpigeon.makeupapp.di

import com.crystalpigeon.makeupapp.BuildConfig
import com.crystalpigeon.makeupapp.model.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideClient(interceptor: HttpLoggingInterceptor): OkHttpClient{
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)

        return if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor).build()
        } else {
            clientBuilder.build()
        }
    }

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://makeup-api.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    @Provides @Singleton
    fun provideMakeUpService (retrofit: Retrofit): MakeUpService = retrofit.create(MakeUpService::class.java)

    @Provides @Singleton
    fun provideDataSource () : DataSource = DataSource()

    @Provides @Singleton
    fun provideMemoryDataSource() : MemoryDataSource = MemoryDataSource()

    @Provides @Singleton
    fun provideDiskDataSource () : DiskDataSource = DiskDataSource()

    @Provides @Singleton
    fun providesNetworkDataSource () : NetworkDataSource = NetworkDataSource()
}