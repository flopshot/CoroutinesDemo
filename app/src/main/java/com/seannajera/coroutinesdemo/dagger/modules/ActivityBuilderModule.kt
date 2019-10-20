package com.seannajera.coroutinesdemo.dagger.modules

import com.seannajera.coroutinesdemo.dagger.scopes.PerActivity
import com.seannajera.coroutinesdemo.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    internal abstract fun bindMainActivity(): MainActivity
}
