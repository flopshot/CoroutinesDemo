package com.seannajera.coroutinesdemo.api

import com.seannajera.coroutinesdemo.persistence.Item
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ItemApi {
    @GET("/users/flopshot/repos")
    fun getItems(): Flow<List<Item>>
}