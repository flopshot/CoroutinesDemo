package com.seannajera.coroutinesdemo.ui.listviews

import android.view.View
import android.widget.TextView
import com.seannajera.coroutinesdemo.R
import com.seannajera.listview.ListView
import com.seannajera.listview.ListViewLayout

class ItemListView(view: View) : ListView<ItemListModel>(view) {
    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.listItemText) }

    override fun onBind(oldModel: ItemListModel?, newModel: ItemListModel) {

        if (oldModel?.item?.title != newModel.item.title) titleView.text = newModel.item.title
    }
}

class ItemListLayout : ListViewLayout() { override val id: Int = R.layout.item  }