package com.seannajera.coroutinesdemo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.seannajera.coroutinesdemo.api.ApiModule
import com.seannajera.coroutinesdemo.persistence.DatabaseModule
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardFragment
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.runBlocking
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
        itemRepository =
            ItemRepository(itemDb, apiModule.getApi(apiModule.retrofit(OkHttpClient(), Gson())))
    }

    @Test
    fun testDashboardFragment() = runBlocking {
        // GIVEN a Dashboard View Model
        // WHEN we launch the Dashboard screen
        launchInContainer(DashboardFragment::class.java, null, object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return DashboardFragment().apply {
                    dashboardViewModel = DashboardViewModel(itemRepository)
                }
            }
        })

        // THEN we expect to see an updated repo list from the view model after a repository sync
        onView(withId(R.id.repo_list)).checkWithTimeout {
            ViewAssertions.matches(atPosition(0,
                ViewMatchers.hasDescendant(withText("Item From Api"))
            ))
        }
    }
}