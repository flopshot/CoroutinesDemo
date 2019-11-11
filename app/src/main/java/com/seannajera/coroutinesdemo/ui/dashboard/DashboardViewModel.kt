package com.seannajera.coroutinesdemo.ui.dashboard

import androidx.lifecycle.ViewModel
import com.seannajera.coroutinesdemo.dagger.ToDagger
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.listview.ItemListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ToDagger
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): Flow<ArrayList<ItemListModel>> =
        itemRepo.getItems().map { items -> ArrayList(items.map { ItemListModel(it) }) }
}
