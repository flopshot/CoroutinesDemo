package com.seannajera.coroutinesdemo.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getCacheFile(context: Context): File {
        return File(context.cacheDir, "okhttp-cache")
    }

    @Singleton
    @Provides
    fun getOkHttpCache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000)
    }

    @Singleton
    @Provides
    fun getGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun getOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()

            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

}
