package com.example.taskmanager.domain.model

data class TaskDraftModel(
    val id: Long,
    val ownerId: String,
    val title: String,
    val description: String,
    val savedAt: Long
)