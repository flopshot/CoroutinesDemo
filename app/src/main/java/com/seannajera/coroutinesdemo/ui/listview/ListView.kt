package com.seannajera.coroutinesdemo.ui.listview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seannajera.coroutinesdemo.R
import com.seannajera.coroutinesdemo.ui.dashboard.ItemListModel

interface ListModel {
    val id: String
    val layout: ListViewLayout
    fun contentSameAs(otherItem: Any): Boolean
}

sealed class ListView<Model : ListModel>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun onBind(model: Model)
}

sealed class ListViewLayout {
    abstract val id: Int

    class ItemListLayout : ListViewLayout() { override val id: Int = R.layout.item  }
}

class ItemListView(view: View) : ListView<ItemListModel>(view) {
    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.listItemText) }

    override fun onBind(model: ItemListModel) {
        titleView.text = model.item.title
    }
}


