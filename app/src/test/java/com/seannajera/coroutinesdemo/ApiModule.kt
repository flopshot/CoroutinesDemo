package com.seannajera.coroutinesdemo

import com.seannajera.coroutinesdemo.api.ItemApi
import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class ApiModule {

    companion object {
        fun getApi(): ItemApi {
            val apiRetrofit = Retrofit.Builder()
                .addCallAdapterFactory(ItemCallAdapterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

            val networkBehavior = NetworkBehavior.create().apply {
                setDelay(1000L, TimeUnit.MILLISECONDS)
                setFailurePercent(0)
            }

            val mockRetrofit = MockRetrofit.Builder(apiRetrofit)
                .networkBehavior(networkBehavior)
                .build()

            val behaviorDelegate = mockRetrofit.create(ItemApi::class.java)

            return MockItemApi(behaviorDelegate)
        }
    }
}

class MockItemApi(private val behaviorDelegate: BehaviorDelegate<ItemApi>) : ItemApi {
    override fun getItems(): Flow<List<Item>> {
        val items = listOf(Item("Item From Api"))
        return behaviorDelegate.returningResponse(flowOf(items)).getItems()
    }
}

class ItemCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit) =

        object : CallAdapter<Any, Any> {

            override fun responseType() = returnType

            override fun adapt(call: Call<Any>) = call.execute().body()!!
        }

    companion object {
        fun create(): CallAdapter.Factory {
            return ItemCallAdapterFactory()
        }
    }
}