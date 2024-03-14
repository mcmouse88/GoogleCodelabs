package com.mcmouse88.paging_advanced.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(
    entities = [RepoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun reposDao(): ReposDao

    companion object {

        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): RepoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RepoDatabase::class.java,
                "github.db"
            ).build()
        }
    }
}