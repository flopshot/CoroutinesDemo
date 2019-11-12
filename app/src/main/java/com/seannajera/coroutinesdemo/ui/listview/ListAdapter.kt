package com.seannajera.coroutinesdemo.ui.listview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seannajera.coroutinesdemo.util.Do
import kotlinx.coroutines.CoroutineScope

class ListAdapter(private val listViewManager: ListViewManager, scope: CoroutineScope): AsyncDiffUtilAdapter<ListView<*>>(scope) {

    private val models: ArrayList<ListModel> = ArrayList()
    private val listViewHash: SparseArray<ListViewLayout> = SparseArray()

    init {
        onListUpdated = { newModels ->
            listViewHash.clear()
            newModels.forEach {
                listViewHash.put(it.layout.id, it.layout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): ListView<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return listViewManager.createListView(listViewHash.get(layoutId), view)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int) {
        listViewManager.bindListView(null, models[position], listView)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(listView, position)
        } else {
            @Suppress("UNCHECKED_CAST")
            val modelState = payloads[0] as Pair<ListModel, ListModel>
            listViewManager.bindListView(modelState.first, modelState.second, listView)
        }
    }

    override fun getItemViewType(position: Int): Int = models[position].layout.id

    override fun getItemCount(): Int = models.size

    fun setListModels(newModels: ArrayList<out ListModel>) = this.updateItems(newModels, models)

}

class ListViewManager {

    fun createListView(layout: ListViewLayout, view: View): ListView<*> {
        return when (layout) {
            is ListViewLayout.ItemListLayout -> ItemListView(view)
            else -> throw IllegalArgumentException("Layout $layout is not instance of ListViewLayout")
        }
    }

    fun bindListView(oldListModel: ListModel?, newListModel: ListModel, listView: ListView<*>) {
        Do exhaustive when (listView) {
            is ItemListView -> listView.onBind(oldListModel as ItemListModel?, newListModel as ItemListModel)
            else -> throw IllegalArgumentException("ListView $listView is not instance of ListView")
        }
    }
}
