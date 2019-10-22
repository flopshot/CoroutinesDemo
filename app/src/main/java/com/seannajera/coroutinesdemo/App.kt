package com.seannajera.coroutinesdemo

import android.app.Application
import android.util.Log
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
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    @Inject lateinit var appDb: AppDatabase

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()

        ComponentInjector.initInjection(this)

        GlobalScope.launch(Dispatchers.IO) {
            Log.w("MyApplication", "Insert \"First\" Item")
            appDb.itemDao().insertAll(Item(title = "First"))
            delay(15000)
            repeat(10) {
                delay(5000)
                val item = Item(title = "${99 - it} A")
                Log.w("MyApplication", "Inserting Another Item: $item")
                appDb.itemDao().insertAll(item)
            }
        }

    }
}