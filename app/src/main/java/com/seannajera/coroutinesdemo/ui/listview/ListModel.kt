package com.seannajera.coroutinesdemo.ui.listview

import com.seannajera.coroutinesdemo.persistence.Item


interface ListModel {
    val id: String
    val layout: ListViewLayout
    fun contentSameAs(otherItem: Any): Boolean
}

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
        get() = ListViewLayout.ItemListLayout()
}
