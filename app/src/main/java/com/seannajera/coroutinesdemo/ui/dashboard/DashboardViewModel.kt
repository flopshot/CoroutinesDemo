package com.seannajera.coroutinesdemo.ui.dashboard

import androidx.lifecycle.ViewModel
import com.seannajera.coroutinesdemo.dagger.ToDagger
import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.coroutinesdemo.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ToDagger
class DashboardViewModel @Inject constructor(private val itemRepo: ItemRepository) : ViewModel() {

    fun fetchText(): Flow<ArrayList<ViewItem>> =
        itemRepo.getItems().map { items -> ArrayList(items.map { ViewItem(it) }) }
}

data class ViewItem(val item: Item): Diffable by DiffItem(item)

class DiffItem(private val item: Item): Diffable {
    override val id: String
        get() = item.title

    override fun contentSameAs(otherItem: Any): Boolean {
        return if (otherItem is Item) {
            otherItem.title == item.title
        } else {
            false
        }
    }
}
