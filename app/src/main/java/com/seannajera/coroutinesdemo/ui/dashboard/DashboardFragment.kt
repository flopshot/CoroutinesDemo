package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.seannajera.coroutinesdemo.R
import com.seannajera.coroutinesdemo.dagger.FromDagger
import com.seannajera.coroutinesdemo.dagger.Injectable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class DashboardFragment : Fragment(), Injectable {

    @Inject @FromDagger lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView = root.findViewById<TextView>(R.id.text_dashboard)
        val bottomTextView = root.findViewById<TextView>(R.id.text_dashboard_bottom)

        dashboardViewModel
            .fetchText()
            .onEach { title ->

                Timber.i("Title From ViewModel: $title for TextView")
                textView.text = title
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)

        dashboardViewModel
            .fetchText()
            .onEach { title ->

                Timber.i("Title From ViewModel: $title for bottom TextView")
                bottomTextView.text = title
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)

        return root
    }
}