package com.seannajera.coroutinesdemo.persistence

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

@Entity
data class Item(
    @SerializedName("name") @PrimaryKey val title: String
)

@Dao
interface ItemDao {
    @Query("SELECT * FROM Item ORDER BY title")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE title IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Item>

    @Query("SELECT * FROM Item WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg items: Item)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: List<Item>)

    @Delete
    fun delete(item: Item)
}

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
