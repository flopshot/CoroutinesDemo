package com.seannajera.coroutinesdemo.ui.components

import android.view.View
import com.seannajera.componentview.ComponentLayout
import com.seannajera.componentview.ComponentManager
import com.seannajera.componentview.ComponentModel
import com.seannajera.componentview.ComponentView
import com.seannajera.coroutinesdemo.util.Do

class AppComponentManager: ComponentManager {

    override fun createView(layout: ComponentLayout, view: View): ComponentView<*> {
        return when (layout) {
            is ItemLayout -> ItemComponentView(view)
            else -> throw IllegalArgumentException("$layout is not instance of ComponentLayout")
        }
    }

    override fun bindView(previousModel: ComponentModel?, currentModel: ComponentModel, componentView: ComponentView<*>) {
        Do exhaustive when (componentView) {
            is ItemComponentView -> componentView.onBind(previousModel as ItemComponentModel?, currentModel as ItemComponentModel)
            else -> throw IllegalArgumentException("$componentView is not instance of ComponentView")
        }
    }
}