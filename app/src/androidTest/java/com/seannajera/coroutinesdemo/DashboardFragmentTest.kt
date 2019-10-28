package com.seannajera.coroutinesdemo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.seannajera.coroutinesdemo.api.ApiModule
import com.seannajera.coroutinesdemo.persistence.DatabaseModule
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardFragment
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardViewModel
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    private lateinit var itemRepository: ItemRepository
    private val apiModule = ApiModule()
    private val databaseModule = DatabaseModule()

    @Before
    fun setup() {
        val itemDb = databaseModule.getAppDatabase(ApplicationProvider.getApplicationContext())
        itemRepository = ItemRepository(itemDb, apiModule.getApi(apiModule.retrofit(OkHttpClient(),Gson())))
    }

    @Test
    fun testDashboardFragment() {
        // GIVEN we launch the dashboard view
        launchInContainer(DashboardFragment::class.java, null, object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return DashboardFragment().apply {
                    dashboardViewModel = DashboardViewModel(itemRepository)
                }
            }
        })

        // THEN verify we get the empty state "NONE" followed by the synced state "Item From Api"
        onView(withId(R.id.text_dashboard)).check(matches(withText("None")))
        Thread.sleep(800L) // TODO: This is a hack.
        onView(withId(R.id.text_dashboard)).check(matches(withText("Item From Api")))
    }
}