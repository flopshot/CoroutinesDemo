package com.seannajera.coroutinesdemo.ui.dashboard

import androidx.lifecycle.ViewModel
import com.seannajera.coroutinesdemo.dagger.ToDagger
import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.listview.ListModel
import com.seannajera.coroutinesdemo.ui.listview.ListViewLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ToDagger
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): Flow<ArrayList<ItemListModel>> =
        itemRepo.getItems().map { items -> ArrayList(items.map { ItemListModel(it) }) }
}

class ItemListModel(val item: Item): ListModel {

    override val id: String
        get() = item.title

    override fun contentSameAs(otherItem: Any): Boolean {
        return if (otherItem is Item) {
            otherItem.title == item.title
        } else {
            false
        }
    }

    override val layout: ListViewLayout
        get() = ListViewLayout.ItemListLayout()
}
