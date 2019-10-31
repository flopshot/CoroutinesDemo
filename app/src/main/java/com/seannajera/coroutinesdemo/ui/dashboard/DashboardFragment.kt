package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seannajera.coroutinesdemo.R
import com.seannajera.coroutinesdemo.dagger.FromDagger
import com.seannajera.coroutinesdemo.dagger.Injectable
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import java.util.Queue
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Inject

class DashboardFragment : Fragment(), Injectable {

    @Inject @FromDagger lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.repo_list)
        val repoAdapter = RepoAdapter()
        recyclerView.adapter = repoAdapter

        dashboardViewModel
            .fetchText()
            .onEach { items ->
                this.lifecycleScope.launch(Dispatchers.Main) {
                    repoAdapter.setItemList(items)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)

        return root
    }
}

class RepoAdapter: AsyncDiffUtilAdapter<RecyclerView.ViewHolder, Item>() {
    private val items: ArrayList<Item> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        ) {}
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.listItemText).text = items[position].title
    }

    override fun itemsDiffUtilCallback(
        oldItems: List<Item>,
        newItems: List<Item>
    ): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldItems[oldItemPosition] == newItems[newItemPosition]

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldItems[oldItemPosition].title == newItems[newItemPosition].title
        }
    }

    fun setItemList(newItems: ArrayList<Item>) {
        if (items.isEmpty()) {
            items.addAll(newItems)
            notifyItemRangeInserted(0, newItems.size)
        } else {
            this.updateItems(newItems, items)
        }
    }
}

abstract class AsyncDiffUtilAdapter<VH : RecyclerView.ViewHolder, ItemType> : RecyclerView.Adapter<VH>() {

    private val pendingItems: Queue<MutableList<ItemType>> = ArrayDeque()
    private var executor: ThreadPoolExecutor? = null
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    /**
     * Sets the thread pool of background threads to be used for the DiffUtil processing
     *
     * @param executor Thread pool executor supplied for executing and updating DiffUtil.Results
     */
    fun setThreadPoolExecutor(executor: ThreadPoolExecutor?) {
        this.executor = executor
    }

    /**
     * Method to be used to update the RecyclerView.Adapter backing data
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     */
    fun updateItems(newItems: MutableList<ItemType>,
        oldItems: MutableList<ItemType>) {
        pendingItems.add(newItems)
        if (pendingItems.size > 1) {
            return
        }

        updateItemsInternal(newItems, oldItems)
    }

    private fun updateItemsInternal(newItems: MutableList<ItemType>, oldItems: MutableList<ItemType>) {

        val calculateDiffUtilRunnable = Runnable {
            val diffResult = DiffUtil.calculateDiff(itemsDiffUtilCallback(oldItems, newItems))

            mainThreadHandler.post({ applyDiffResult(newItems, oldItems, diffResult) })
        }

        if (executor != null) {
            executor!!.execute(calculateDiffUtilRunnable)
        } else {
            AsyncTask.execute(calculateDiffUtilRunnable)
        }

    }

    private fun applyDiffResult(newItems: MutableList<ItemType>, oldItems: MutableList<ItemType>,
        diffResult: DiffUtil.DiffResult) {
        pendingItems.remove()
        dispatchUpdates(newItems, oldItems, diffResult)
        if (pendingItems.size > 0) {
            updateItemsInternal(pendingItems.peek(), newItems)
        }
    }

    private fun dispatchUpdates(newItems: List<ItemType>, oldItems: MutableList<ItemType>,
        diffResult: DiffUtil.DiffResult) {
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
    protected abstract fun itemsDiffUtilCallback(oldItems: List<ItemType>,
        newItems: List<ItemType>): DiffUtil.Callback

}
