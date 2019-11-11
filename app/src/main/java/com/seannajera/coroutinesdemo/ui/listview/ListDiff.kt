package com.seannajera.coroutinesdemo.ui.listview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import java.util.Queue

abstract class AsyncDiffUtilAdapter<VH : RecyclerView.ViewHolder>(private val scope: CoroutineScope) :
    RecyclerView.Adapter<VH>() {

    private val pendingItems: Queue<MutableList<*>> = ArrayDeque()

    /**
     * Method to be used to update the RecyclerView.Adapter backing data
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     */
    protected fun updateItems(
        newItems: MutableList<out ListModel>,
        oldItems: MutableList<ListModel>
    ) {
        pendingItems.add(newItems)
        if (pendingItems.size > 1) {
            return
        }

        updateItemsInternal(newItems, oldItems)
    }

    private fun updateItemsInternal(
        newItems: MutableList<out ListModel>,
        oldItems: MutableList<ListModel>
    ) {
        scope.launch(Dispatchers.Default) {
            val diffResult = DiffUtil.calculateDiff(itemsDiffUtilCallback(oldItems, newItems))

            launch(Dispatchers.Main) {
                applyDiffResult(newItems, oldItems, diffResult)
            }
        }
    }

    private fun applyDiffResult(
        newItems: MutableList<out ListModel>, oldItems: MutableList<ListModel>,
        diffResult: DiffUtil.DiffResult
    ) {
        pendingItems.remove()
        dispatchUpdates(newItems, oldItems, diffResult)
        pendingItems.peek()?.let {
            if (pendingItems.size > 0) {
                @Suppress("UNCHECKED_CAST")
                updateItemsInternal(it as MutableList<ListModel>, oldItems)
            }
        }
    }

    private fun dispatchUpdates(
        newItems: List<ListModel>, oldItems: MutableList<ListModel>,
        diffResult: DiffUtil.DiffResult
    ) {
        diffResult.dispatchUpdatesTo(this)
        oldItems.clear()
        oldItems.addAll(newItems)
    }

    /**
     * This method implements the DiffUtil.Callback between the new and old data items for the
     * RecyclerView.Adapter
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     * @return the DiffUtil.Callback used to calculate the DiffUtil.Result in the background thread
     */
    private fun itemsDiffUtilCallback(
        oldItems: List<ListModel>,
        newItems: List<ListModel>
    ): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].id == newItems[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].contentSameAs(newItems[newItemPosition])

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                Pair(oldItems[oldItemPosition], newItems[newItemPosition])
        }
    }
}
