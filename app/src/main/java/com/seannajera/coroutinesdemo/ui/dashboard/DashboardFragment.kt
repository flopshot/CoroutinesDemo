package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import java.util.Queue
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
        val repoAdapter = RepoAdapter(this.lifecycleScope)
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

class RepoAdapter(scope: CoroutineScope) : AsyncDiffUtilAdapter<RecyclerView.ViewHolder>(scope) {
    private val items: ArrayList<ViewItem> = arrayListOf()

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
        holder.itemView.findViewById<TextView>(R.id.listItemText).text = items[position].item.title
    }

    fun setItemList(newItems: ArrayList<ViewItem>) {
        this.updateItems(newItems, items)
    }
}

abstract class AsyncDiffUtilAdapter<VH: RecyclerView.ViewHolder>(private val scope: CoroutineScope) :
    RecyclerView.Adapter<VH>() {

    private val pendingItems: Queue<MutableList<*>> = ArrayDeque()

    /**
     * Method to be used to update the RecyclerView.Adapter backing data
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     */
    protected fun <T : Diffable> updateItems(
        newItems: MutableList<T>,
        oldItems: MutableList<T>
    ) {
        pendingItems.add(newItems)
        if (pendingItems.size > 1) {
            return
        }

        updateItemsInternal(newItems, oldItems)
    }

    private fun <T : Diffable> updateItemsInternal(
        newItems: MutableList<T>,
        oldItems: MutableList<T>
    ) {
        scope.launch(Dispatchers.Default) {
            val diffResult = DiffUtil.calculateDiff(itemsDiffUtilCallback(oldItems, newItems))

            launch(Dispatchers.Main) {
                applyDiffResult(newItems, oldItems, diffResult)
            }
        }
    }

    private fun <T : Diffable> applyDiffResult(
        newItems: MutableList<T>, oldItems: MutableList<T>,
        diffResult: DiffUtil.DiffResult
    ) {
        pendingItems.remove()
        dispatchUpdates(newItems, oldItems, diffResult)
        pendingItems.peek()?.let {
            if (pendingItems.size > 0) {
                @Suppress("UNCHECKED_CAST")
                updateItemsInternal(it as MutableList<T>, newItems)
            }
        }
    }

    private fun <T : Diffable> dispatchUpdates(
        newItems: List<T>, oldItems: MutableList<T>,
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
    private fun <T : Diffable> itemsDiffUtilCallback(
        oldItems: List<T>,
        newItems: List<T>
    ): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].id == newItems[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].contentSameAs(newItems[newItemPosition])
        }
    }
}

interface Diffable {

    val id: String

    fun contentSameAs(otherItem: Any): Boolean
}
