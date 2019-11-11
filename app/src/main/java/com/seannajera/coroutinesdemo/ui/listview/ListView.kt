package com.seannajera.coroutinesdemo.ui.listview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seannajera.coroutinesdemo.R

sealed class ListView<Model : ListModel>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun onBind(oldModel: Model?, newModel: Model)
}

sealed class ListViewLayout {
    abstract val id: Int

    class ItemListLayout : ListViewLayout() { override val id: Int = R.layout.item  }
}

class ItemListView(view: View) : ListView<ItemListModel>(view) {
    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.listItemText) }

    override fun onBind(oldModel: ItemListModel?, newModel: ItemListModel) {
        
        if (oldModel?.item?.title != newModel.item.title) titleView.text = newModel.item.title
    }
}


