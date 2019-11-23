package com.seannajera.coroutinesdemo.ui.components

import com.seannajera.componentview.ComponentLayout
import com.seannajera.componentview.ComponentModel
import com.seannajera.coroutinesdemo.persistence.Item

data class ItemComponentModel(val item: Item): ComponentModel {

    override val id: String = item.title

    override val layout: ComponentLayout = ItemLayout()

    override fun contentSameAs(otherItem: Any): Boolean {
        return if (otherItem is ItemComponentModel) {
            otherItem.item.title == item.title
        } else {
            false
        }
    }
}
