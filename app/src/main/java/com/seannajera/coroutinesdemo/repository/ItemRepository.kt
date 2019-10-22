package com.seannajera.coroutinesdemo.repository

import android.util.Log
import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val db: AppDatabase, private val api: ItemApi) {

    fun getItems(): Flow<List<Item>> {
        return db.itemDao().getAll()
            .take(1)
            .onCompletion {
                Log.w("Repo","First Items from DB: $it")
                emitAll(api.getItems().onEach { items ->
                    db.itemDao().insertAll(items)
                    Log.w("Repo", "Items from api: $items")
                }.flatMapLatest {
                    Log.w("Repo", "After api Items from DB")
                    db.itemDao().getAll()
                })
            }
            .catch{ e -> Log.w("Repo", "Caught Exception: ${e.localizedMessage}")}
    }
}
