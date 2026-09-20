package com.example.taskmanager.data.remote.dto

data class TaskDocument(
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)