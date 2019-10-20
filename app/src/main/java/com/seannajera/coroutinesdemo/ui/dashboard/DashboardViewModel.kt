package com.seannajera.coroutinesdemo.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.seannajera.coroutinesdemo.dagger.Bound
import com.seannajera.coroutinesdemo.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Bound
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): LiveData<String> = liveData {
        itemRepo
            .getItems()
            .flowOn(Dispatchers.IO)
            .collect {
                val title = it.firstOrNull()?.title
                val emittedTitle = if (title.isNullOrBlank()) "BadNetwork" else title
                emit(emittedTitle)
            }
    }
}
