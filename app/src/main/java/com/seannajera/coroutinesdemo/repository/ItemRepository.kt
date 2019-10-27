package com.seannajera.coroutinesdemo.repository

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
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val db: AppDatabase, private val api: ItemApi) {

    private val itemSyncExecutor = object : ModelSyncExecutor<List<Item>>() {

        override fun syncToPersistence(model: List<Item>) = db.itemDao().insertAll(model)

        override fun loadFromPersistence() = db.itemDao().getAll()

        override fun loadFromNetwork() = api.getItems()
    }

    fun getItems(): Flow<List<Item>> = itemSyncExecutor.reactiveModel
}

abstract class ModelSyncExecutor<ModelType> {

    private var requestInFlight = AtomicBoolean(false)

    val reactiveModel: Flow<ModelType> =
        initFromPersistence()
            .take(1)
            .onCompletion {
                emitAll(
                    syncAndReturn()
                )
            }
            .catch { e ->
                Timber.i("reactiveModel: ${e.localizedMessage}")
            }

    protected abstract fun syncToPersistence(model: ModelType)

    protected abstract fun loadFromPersistence(): Flow<ModelType>

    protected abstract fun loadFromNetwork(): Flow<ModelType>

    protected fun initFromPersistence(): Flow<ModelType> =loadFromPersistence()

    protected fun onNetworkFailed(t: Throwable) = Timber.e(t)

    private fun syncAndReturn(): Flow<ModelType> {
        return if (!requestInFlight.get()) {

            requestInFlight.set(true)

            loadFromNetwork()
                .onEach { modelBeforeCall ->

                    Timber.i("Model from api: $modelBeforeCall")
                    syncToPersistence(modelBeforeCall)
                }.flatMapLatest { modelAfterCall ->

                    requestInFlight.set(false)
                    Timber.i("After api Model from DB: $modelAfterCall")
                    loadFromPersistence()
                }.catch { e ->

                    requestInFlight.set(false)
                    onNetworkFailed(e)
                    loadFromPersistence()
                }
        } else {
            loadFromPersistence()
        }
    }
}


