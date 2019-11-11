package com.seannajera.coroutinesdemo

import android.app.Application
import com.seannajera.coroutinesdemo.dagger.ComponentInjector
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.persistence.Item
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject lateinit var appDb: AppDatabase

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()

        ComponentInjector.initInjection(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        GlobalScope.launch(Dispatchers.IO) {
            appDb.itemDao().deleteAll()
            delay(3000)
            repeat(10) {
                delay(3000)
                val item = Item(title = "${99 - it} A")
                appDb.itemDao().insertAll(item)
            }
        }
    }
}