package com.seannajera.coroutinesdemo.repository

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ibm.icu.impl.Assert.fail
import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.api.ItemCallAdapterFactory
import com.seannajera.coroutinesdemo.api.MockItemApi
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class ItemRepositoryTest {

    private lateinit var itemRepository: ItemRepository
    private lateinit var itemDb: AppDatabase

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(ItemCallAdapterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()

        val networkBehavior = NetworkBehavior.create().apply {
            setDelay(0L, TimeUnit.MILLISECONDS)
            setFailurePercent(0)
        }

        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(networkBehavior)
            .build()

        val behaviorDelegate = mockRetrofit.create(ItemApi::class.java)

        itemDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        itemRepository = ItemRepository(itemDb, MockItemApi(behaviorDelegate))
    }

    @Test
    fun getItemFromRepository() = runBlocking<Unit> {
        val dbItems = listOf(Item("Item From DB 1"), Item("Item From DB 2"))
        val dbItemsAfterApiSync = listOf(Item("Item From Api"), Item("Item From DB 1"), Item("Item From DB 2"))
        itemDb.itemDao().insertAll(dbItems)

       val flowFromRepo = itemRepository.getItems()

        var i = 0
        flowFromRepo.take(2)
            .flowOn(Dispatchers.Unconfined)
            .onEach{
                if (i == 0) {
                    if (it != dbItems) fail("$dbItems does not equal $it")
                    i++
                } else {
                    if (it != dbItemsAfterApiSync) fail("$dbItemsAfterApiSync does not equal $it")
                }
            }
            .launchIn(this)
    }
}