package com.seannajera.coroutinesdemo.dagger.components

import android.app.Application
import com.seannajera.coroutinesdemo.App
import com.seannajera.coroutinesdemo.AppModule
import com.seannajera.coroutinesdemo.api.ApiModule
import com.seannajera.coroutinesdemo.dagger.modules.ActivityBuilderModule
import com.seannajera.coroutinesdemo.persistence.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBuilderModule::class,
        ApiModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun injectApplicationComponent(application: App)
}
