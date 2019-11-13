package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.seannajera.coroutinesdemo.R
import com.seannajera.coroutinesdemo.dagger.FromDagger
import com.seannajera.coroutinesdemo.dagger.Injectable
import com.seannajera.coroutinesdemo.ui.listviews.ListViewManager
import com.seannajera.listview.ListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardFragment : Fragment(), Injectable {

    @Inject @FromDagger lateinit var dashboardViewModel: DashboardViewModel
    val listViewManager = ListViewManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.repo_list)
        val repoAdapter = ListAdapter(listViewManager, this.lifecycleScope)
        recyclerView.adapter = repoAdapter

        dashboardViewModel
            .fetchText()
            .onEach { items ->
                this.lifecycleScope.launch(Dispatchers.Main) {
                    repoAdapter.setListModels(items)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)

        return root
    }
}

