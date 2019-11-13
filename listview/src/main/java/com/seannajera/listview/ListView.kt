package com.seannajera.listview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

// This class should a sealed class. However, we can't use sealed classes because inherited class
// must exist in the same file, disallowing for extraction into library can make sealed class
// after this ticket is done https://youtrack.jetbrains.com/issue/KT-13495
abstract class ListView<Model : ListModel>(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun onBind(oldModel: Model?, newModel: Model)
}

abstract class ListViewLayout {
    abstract val id: Int
}