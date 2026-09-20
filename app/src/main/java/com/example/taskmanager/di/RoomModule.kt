package com.example.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanager.data.local.dao.DraftDao
import com.example.taskmanager.data.local.database.TaskManagerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideTaskManagerDatabase(
        @ApplicationContext context: Context
    ): TaskManagerDatabase {

        return Room.databaseBuilder(
            context,
            TaskManagerDatabase::class.java,
            "task_manager_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDraftDao(
        database: TaskManagerDatabase
    ): DraftDao {

        return database.draftDao()
    }
}