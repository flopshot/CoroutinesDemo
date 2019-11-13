package com.seannajera.coroutinesdemo.ui.listviews

import android.view.View
import com.seannajera.coroutinesdemo.util.Do
import com.seannajera.listview.ListManager
import com.seannajera.listview.ListModel
import com.seannajera.listview.ListView
import com.seannajera.listview.ListViewLayout

class ListViewManager: ListManager {

    override fun createListView(layout: ListViewLayout, view: View): ListView<*> {
        return when (layout) {
            is ItemListLayout -> ItemListView(view)
            else -> throw IllegalArgumentException("Layout $layout is not instance of ListViewLayout")
        }
    }

    override fun bindListView(oldListModel: ListModel?, newListModel: ListModel, listView: ListView<*>) {
        Do exhaustive when (listView) {
            is ItemListView -> listView.onBind(oldListModel as ItemListModel?, newListModel as ItemListModel)
            else -> throw IllegalArgumentException("ListView $listView is not instance of ListView")
        }
    }
}