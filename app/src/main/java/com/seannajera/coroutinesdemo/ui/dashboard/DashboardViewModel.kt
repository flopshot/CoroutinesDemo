package com.seannajera.coroutinesdemo.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.seannajera.coroutinesdemo.dagger.Bound
import com.seannajera.coroutinesdemo.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Bound
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): Flow<String> =
        itemRepo
            .getItems()
            .map {

                Log.i("DashboardViewModel", "Items From Repo: $it")
                val title = it.firstOrNull()?.title
                if (title.isNullOrBlank()) "BadNetwork" else title
            }
}
