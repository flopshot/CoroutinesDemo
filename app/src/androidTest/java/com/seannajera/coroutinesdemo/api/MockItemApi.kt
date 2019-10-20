package com.seannajera.coroutinesdemo.api

import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import java.lang.reflect.Type

class MockItemApi(private val behaviorDelegate: BehaviorDelegate<ItemApi>) : ItemApi {
    override fun getItems(): Flow<List<Item>> {
        val items = listOf(Item("Some Title"))
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

