package com.mcmouse88.android_privacy.di

import android.content.Context
import androidx.room.Room
import com.mcmouse88.android_privacy.data.local.dao.LogDao
import com.mcmouse88.android_privacy.data.local.database.AppDatabase
import com.mcmouse88.android_privacy.data.local.repository.MediaRepositoryImpl
import com.mcmouse88.android_privacy.data.local.repository.PhotoSaverRepositoryImpl
import com.mcmouse88.android_privacy.domain.repository.MediaRepository
import com.mcmouse88.android_privacy.domain.repository.PhotoSaverRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object MainModule {

    @[Provides Singleton]
    fun provideMediaRepository(impl: MediaRepositoryImpl): MediaRepository {
        return impl
    }

    @[Provides Singleton]
    fun providePhotoSaverRepository(impl: PhotoSaverRepositoryImpl): PhotoSaverRepository {
        return impl
    }

    @[Provides Singleton]
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    @[Provides Singleton]
    fun provideLogDao(
        db: AppDatabase
    ): LogDao {
        return db.logDao()
    }
}