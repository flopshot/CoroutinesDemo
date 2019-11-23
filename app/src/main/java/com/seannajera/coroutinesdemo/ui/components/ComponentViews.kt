package com.seannajera.coroutinesdemo.ui.components

import android.view.View
import android.widget.TextView
import com.seannajera.componentview.ComponentLayout
import com.seannajera.componentview.ComponentView
import com.seannajera.coroutinesdemo.R

class ItemComponentView(view: View) : ComponentView<ItemComponentModel>(view) {
    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.listItemText) }

    override fun onBind(previousModel: ItemComponentModel?, currentModel: ItemComponentModel) {
        if (previousModel?.item?.title != currentModel.item.title) titleView.text = currentModel.item.title
    }
}

data class ItemLayout(override val id: Int = R.layout.item) : ComponentLayout()