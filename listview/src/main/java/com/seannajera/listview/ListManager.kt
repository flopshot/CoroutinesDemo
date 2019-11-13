package com.seannajera.listview

import android.view.View

interface ListManager {
    fun createListView(layout: ListViewLayout, view: View): ListView<*>
    fun bindListView(oldListModel: ListModel?, newListModel: ListModel, listView: ListView<*>)
}