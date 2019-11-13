package com.seannajera.coroutinesdemo.ui.listviews

import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.listview.ListModel
import com.seannajera.listview.ListViewLayout

class ItemListModel(val item: Item): ListModel {

    override val id: String
        get() = item.title

    override fun contentSameAs(otherItem: Any): Boolean {
        return if (otherItem is ItemListModel) {
            otherItem.item.title == item.title
        } else {
            false
        }
    }

    override val layout: ListViewLayout
        get() = ItemListLayout()
}
