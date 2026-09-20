package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_drafts")
data class TaskDraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val ownerId: String,
    val title: String,
    val description: String,
    val savedAt: Long
)