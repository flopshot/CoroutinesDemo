package com.seannajera.coroutinesdemo.ui.dashboard

import androidx.lifecycle.ViewModel
import com.seannajera.coroutinesdemo.dagger.ToDagger
import com.seannajera.coroutinesdemo.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@ToDagger
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): Flow<String> =
        itemRepo
            .getItems()
            .map {

                Timber.i("Items From Repo: $it")
                val title = it.firstOrNull()?.title
                if (title.isNullOrBlank()) "None" else title
            }
}
