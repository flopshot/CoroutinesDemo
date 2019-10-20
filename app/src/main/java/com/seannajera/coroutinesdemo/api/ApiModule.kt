package com.seannajera.coroutinesdemo.api

import com.google.gson.Gson
import com.seannajera.coroutinesdemo.FlowCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun getApi(apiRetrofit: Retrofit): ItemApi {
        return apiRetrofit.create<ItemApi>(ItemApi::class.java)
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()
    }
}