package com.seannajera.coroutinesdemo.ui.dashboard

import android.os.Bundle
import android.util.Log
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
        val textView = root.findViewById<TextView>(R.id.text_dashboard)
        val bottomTextView = root.findViewById<TextView>(R.id.text_dashboard_bottom)
        dashboardViewModel
            .fetchText()
            .observe(this::getLifecycle) { title ->
                Log.w("DashboardFragment","Title From ViewModel: $title for TextView")
                textView.text = title
            }

        dashboardViewModel
            .fetchText()
            .observe(this::getLifecycle) { title ->
                Log.w("DashboardFragment","Title From ViewModel: $title bottom TextView")
                bottomTextView.text = title
            }

        return root
    }
}