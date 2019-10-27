package com.seannajera.coroutinesdemo.api

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun getApi(apiRetrofit: Retrofit): ItemApi {
        val networkBehavior = NetworkBehavior.create().apply {
            setDelay(1000L, TimeUnit.MILLISECONDS)
            setFailurePercent(0)
        }

        val mockRetrofit = MockRetrofit.Builder(apiRetrofit)
            .networkBehavior(networkBehavior)
            .build()

        val behaviorDelegate = mockRetrofit.create(ItemApi::class.java)

        return MockItemApi(behaviorDelegate)
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(ItemCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.github.com/")
            .build()
    }
}