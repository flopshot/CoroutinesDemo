package com.seannajera.coroutinesdemo.repository

import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.persistence.AppDatabase
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val db: AppDatabase, private val api: ItemApi) {

    fun getItems(): Flow<List<Item>> {
        return db.itemDao().getAll().flatMapLatest {
            api.getItems().onEach { items ->
                items.forEach { item ->
                    db.itemDao().insertAll(item)
                }
            }.flatMapLatest {
                db.itemDao().getAll()
            }
        }
    }
}
