package com.seannajera.coroutinesdemo.repository

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.api.ItemsCallAdapterFactory
import com.seannajera.coroutinesdemo.api.TestItemApi
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.toList
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
            .addCallAdapterFactory(ItemsCallAdapterFactory.create())
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

        itemRepository = ItemRepository(itemDb, TestItemApi(behaviorDelegate))
    }

    @Test
    fun getItemFromRepository() = runBlocking {
        val dbItems = listOf(Item("Item From DB 1"), Item("Item From DB 2"))
        val dbItemsAfterApiSync = listOf(Item("Item From Api"), Item("Item From DB 1"), Item("Item From DB 2"))
        itemDb.itemDao().insertAll(dbItems)

        val itemsFromRepo = itemRepository.getItems()

        assert(itemsFromRepo.toList() == listOf(dbItems, dbItemsAfterApiSync))
    }
}