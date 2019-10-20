package com.seannajera.coroutinesdemo.dagger.modules

import androidx.lifecycle.ViewModelStoreOwner
import com.seannajera.coroutinesdemo.dagger.scopes.PerFragment
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardFragment
import com.seannajera.coroutinesdemo.ui.viewmodel.ViewModelModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [DashboardFragmentModule::class, ViewModelModule::class])
    internal abstract fun bindDashboardFragment(): DashboardFragment

}

@Module
abstract class DashboardFragmentModule {
    @Binds
    abstract fun provideStoreOwner(fragment: DashboardFragment): ViewModelStoreOwner
}
