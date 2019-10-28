package com.seannajera.coroutinesdemo.repository

import android.os.Build
import com.seannajera.coroutinesdemo.ApiModule
import com.seannajera.coroutinesdemo.DatabaseModule
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
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class ItemRepositoryTest {

    private lateinit var itemRepository: ItemRepository
    private lateinit var itemDb: AppDatabase

    @Before
    fun setup() {
        itemDb = DatabaseModule.getDatabase(RuntimeEnvironment.application)
        itemRepository = ItemRepository(itemDb, ApiModule.getApi())
    }

    @Test
    fun syncItemsFromNetworkToDatabase() = runBlocking<Unit> {

        // GIVEN
        val dbItems = listOf(Item("Item From DB 1"), Item("Item From DB 2"))
        itemDb.itemDao().insertAll(dbItems)

        // WHEN
        val flowFromRepo = itemRepository.getItems()
        val dbItemsAfterApiSync =
            listOf(Item("Item From Api"), Item("Item From DB 1"), Item("Item From DB 2")) // itemRepository.getItems() will fetch an Item and insert it into the database

        // THEN
        var i = 0
        flowFromRepo.take(2)
            .flowOn(Dispatchers.Unconfined)
            .onEach {
                if (i == 0) {
                    assert(it == dbItems) { "$dbItems does not equal $it" }
                    i++
                } else {
                    assert(it == dbItemsAfterApiSync) { "$dbItemsAfterApiSync does not equal $it" }
                }
            }
            .launchIn(this)
    }
}