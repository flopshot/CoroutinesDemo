package com.seannajera.coroutinesdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.seannajera.coroutinesdemo.dagger.FromDagger
import com.seannajera.coroutinesdemo.dagger.ToDagger
import com.seannajera.coroutinesdemo.dagger.scopes.PerFragment
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    internal abstract fun bindDashboardViewModel(@ToDagger viewModel: DashboardViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
class ViewModelModule {
    @Provides
    @PerFragment
    @FromDagger
    internal fun getDashboardViewModel(
        factory: ViewModelProvider.Factory,
        storeOwner: ViewModelStoreOwner
    ): DashboardViewModel {
        return ViewModelProvider(storeOwner, factory).get(DashboardViewModel::class.java)
    }
}