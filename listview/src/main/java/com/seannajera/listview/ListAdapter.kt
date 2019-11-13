package com.seannajera.listview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope

class ListAdapter(private val listManager: ListManager, scope: CoroutineScope): AsyncDiffUtilAdapter<ListView<*>>(scope) {

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

        return listManager.createListView(listViewHash.get(layoutId), view)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int) {
        listManager.bindListView(null, models[position], listView)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(listView, position)
        } else {
            @Suppress("UNCHECKED_CAST")
            val modelState = payloads[0] as Pair<ListModel, ListModel>
            listManager.bindListView(modelState.first, modelState.second, listView)
        }
    }

    override fun getItemViewType(position: Int): Int = models[position].layout.id

    override fun getItemCount(): Int = models.size

    fun setListModels(newModels: ArrayList<out ListModel>) = this.updateItems(newModels, models)

}