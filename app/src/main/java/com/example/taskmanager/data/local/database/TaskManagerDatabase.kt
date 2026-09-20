package com.example.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.local.dao.DraftDao
import com.example.taskmanager.data.local.entity.TaskDraftEntity

@Database(
    entities = [TaskDraftEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskManagerDatabase : RoomDatabase() {

    abstract fun draftDao(): DraftDao
}