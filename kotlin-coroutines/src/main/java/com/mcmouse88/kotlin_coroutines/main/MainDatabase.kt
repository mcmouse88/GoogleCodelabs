package com.mcmouse88.kotlin_coroutines.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Title represents the title fetched from the network
 */
@Entity
data class Title constructor(val title: String, @PrimaryKey val id: Int = 0)

/**
 * Very small database that will hold one title
 */
@Dao
interface TitleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: Title)

    @get:Query("SELECT * FROM Title WHERE id = 0")
    val titleLiveData: LiveData<Title?>
}

/**
 * TitleDatabase provides a reference to the dao to repositories
 */
@Database(entities = [Title::class], version = 1, exportSchema = false)
abstract class TitleDatabase : RoomDatabase() {
    abstract val titleDao: TitleDao
}

private lateinit var INSTANCE: TitleDatabase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): TitleDatabase {
    synchronized(TitleDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    TitleDatabase::class.java,
                    "tittles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}