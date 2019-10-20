package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.seannajera.coroutinesdemo.R
import com.seannajera.coroutinesdemo.dagger.Injectable
import com.seannajera.coroutinesdemo.dagger.Provided
import javax.inject.Inject

class DashboardFragment : Fragment(), Injectable {

    @Inject @Provided lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel
            .fetchText()
            .observe(this::getLifecycle) { newVal ->
                textView.text = newVal
            }
        return root
    }
}