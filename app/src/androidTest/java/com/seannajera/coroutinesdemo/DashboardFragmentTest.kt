package com.seannajera.coroutinesdemo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.api.ItemCallAdapterFactory
import com.seannajera.coroutinesdemo.api.MockItemApi
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardFragment
import com.seannajera.coroutinesdemo.ui.dashboard.DashboardViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    private lateinit var itemRepository: ItemRepository

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(ItemCallAdapterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()

        val networkBehavior = NetworkBehavior.create().apply {
            setDelay(800L, TimeUnit.MILLISECONDS)
            setFailurePercent(0)
        }

        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(networkBehavior)
            .build()

        val behaviorDelegate = mockRetrofit.create(ItemApi::class.java)

        val itemDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        itemRepository = ItemRepository(itemDb, MockItemApi(behaviorDelegate))
    }

    @Test
    fun testDashboardFragment() {
        launchInContainer(DashboardFragment::class.java, null, object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return DashboardFragment().apply {
                    dashboardViewModel = DashboardViewModel(itemRepository)
                }
            }
        })
        Thread.sleep(1200L) // TODO: This is a hack. Instead of LiveData, maybe just use Flow
        onView(withId(R.id.text_dashboard)).check(matches(withText("Some Title")))
    }
}