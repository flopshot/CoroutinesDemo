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

    private val itemSyncExecutor = object : ModelSyncExecutor<List<Item>>() {

        override fun syncToPersistence(model: List<Item>) = db.itemDao().insertAll(model)

        override fun loadFromPersistence() = db.itemDao().getAll()

        override fun loadFromNetwork() = api.getItems()
    }

    fun getItems(): Flow<List<Item>> = itemSyncExecutor.getModelFlow()
}

abstract class ModelSyncExecutor<ModelType> {

    @Volatile var requestInFlight = false

    fun getModelFlow(): Flow<ModelType> =
        initFromPersistence()
            .take(1)
            .onCompletion {
                emitAll(
                    syncAndFlow()
                )
            }
            .catch { e -> Log.w("Repo", "Caught Exception: ${e.localizedMessage}") }

    protected abstract fun syncToPersistence(model: ModelType)

    protected abstract fun loadFromPersistence(): Flow<ModelType>

    protected abstract fun loadFromNetwork(): Flow<ModelType>

    protected fun initFromPersistence(): Flow<ModelType> {
        return loadFromPersistence()
    }

    protected fun onNetworkFailed(t: Throwable) {
        Log.e("Api", "Error", t)
    }

    private fun syncAndFlow(): Flow<ModelType> {
        return if (!requestInFlight) {

            requestInFlight = true

            loadFromNetwork()
                .onEach { modelBeforeCall ->
                    syncToPersistence(modelBeforeCall)
                    requestInFlight = false
                    Log.w("Repo", "Model from api: $modelBeforeCall")
                }.flatMapLatest { modelAfterCall ->
                    Log.w("Repo", "After api Model from DB: $modelAfterCall")
                    loadFromPersistence()
                }.catch { e ->
                    requestInFlight = false
                    onNetworkFailed(e)
                    loadFromPersistence()
                }
        } else {
            loadFromPersistence()
        }
    }
}


