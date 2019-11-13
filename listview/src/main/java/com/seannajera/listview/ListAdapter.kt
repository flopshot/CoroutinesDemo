package com.seannajera.listview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ListAdapter(private val listManager: ListManager) :
    ListAdapter<ListModel, ListView<*>>(listDiffer) {

    private val listViewHash: SparseArray<ListViewLayout> = SparseArray()

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): ListView<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return listManager.createListView(listViewHash.get(layoutId), view)
    }

    override fun onBindViewHolder(listView: ListView<*>, position: Int) {
        listManager.bindListView(null, getItem(position), listView)
    }

    override fun onBindViewHolder(
        listView: ListView<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(listView, position)
        } else {
            @Suppress("UNCHECKED_CAST")
            val modelState = payloads[0] as Pair<ListModel, ListModel>
            listManager.bindListView(modelState.first, modelState.second, listView)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).layout.id

    override fun onCurrentListChanged(
        previousList: MutableList<ListModel>, currentList: MutableList<ListModel>
    ) {
        listViewHash.clear()
        getCurrentList().forEach {
            listViewHash.put(it.layout.id, it.layout)
        }
    }

    fun setListModels(newModels: ArrayList<out ListModel>) = submitList(newModels)

    companion object {
        val listDiffer = object : DiffUtil.ItemCallback<ListModel>() {
            override fun areItemsTheSame(oldModel: ListModel, newModel: ListModel): Boolean {
                return oldModel.id == newModel.id
            }

            override fun areContentsTheSame(oldModel: ListModel, newModel: ListModel): Boolean {
                return oldModel.contentSameAs(newModel)
            }
        }
    }
}
