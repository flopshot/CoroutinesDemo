package com.seannajera.coroutinesdemo.ui.listview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seannajera.coroutinesdemo.R

// This class should a sealed class. However, we can't use sealed classes because inherited class
// must exist in the same file, disallowing for extraction into library can make sealed class
// after this ticket is done https://youtrack.jetbrains.com/issue/KT-13495
abstract class ListView<Model : ListModel>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun onBind(oldModel: Model?, newModel: Model)
}

abstract class ListViewLayout {
    abstract val id: Int

    class ItemListLayout : ListViewLayout() { override val id: Int = R.layout.item  }
}

class ItemListView(view: View) : ListView<ItemListModel>(view) {
    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.listItemText) }

    override fun onBind(oldModel: ItemListModel?, newModel: ItemListModel) {
        
        if (oldModel?.item?.title != newModel.item.title) titleView.text = newModel.item.title
    }
}


