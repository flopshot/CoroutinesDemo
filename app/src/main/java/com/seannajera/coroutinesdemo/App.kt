package com.seannajera.coroutinesdemo

import android.app.Application
import com.seannajera.coroutinesdemo.dagger.ComponentInjector
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
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

//        GlobalScope.launch(Dispatchers.IO) {
//            Log.w("MyApplication", "Insert \"First\" Item")
//            appDb.itemDao().insertAll(Item(title = "First"))
//            delay(15000)
//            repeat(10) {
//                delay(5000)
//                val item = Item(title = "${99 - it} A")
//                Log.w("MyApplication", "Inserting Another Item: $item")
//                appDb.itemDao().insertAll(item)
//            }
//        }
    }
}