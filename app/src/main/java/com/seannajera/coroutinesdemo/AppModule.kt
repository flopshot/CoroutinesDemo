package com.seannajera.coroutinesdemo

import android.app.Application
import android.content.Context
import com.seannajera.coroutinesdemo.ui.viewmodel.ViewModelFactoryModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}
