package com.seannajera.coroutinesdemo

import android.content.Context
import androidx.room.Room
import com.seannajera.coroutinesdemo.persistence.AppDatabase

class DatabaseModule {

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
            ).allowMainThreadQueries().build()
        }
    }
}