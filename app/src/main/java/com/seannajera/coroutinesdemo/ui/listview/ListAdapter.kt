package com.seannajera.coroutinesdemo.ui.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seannajera.coroutinesdemo.ui.dashboard.ItemListModel
import kotlinx.coroutines.CoroutineScope

class ListAdapter(private val listViewManager: ListViewManager, scope: CoroutineScope): AsyncDiffUtilAdapter<ListView<*>>(scope) {

    private val models: ArrayList<ListModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListView<*> {
        val layout = models[position].layout

        val view = LayoutInflater.from(parent.context)
            .inflate(layout.id, parent, false)

        return listViewManager.createListView(layout, view)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int) {
        listViewManager.bindListView(models[position], listView)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int = models.size

    fun setListModels(newModels: ArrayList<out ListModel>) = this.updateItems(newModels, models)

}

class ListViewManager {

    fun createListView(layout: ListViewLayout, view: View): ListView<*> {
        return when (layout) {
            is ListViewLayout.ItemListLayout -> ItemListView(view)
        }
    }

    fun bindListView(listModel: ListModel, listView: ListView<*>) {
        when (listView) {
            is ItemListView -> listView.onBind(listModel as ItemListModel)
        }
    }
}
