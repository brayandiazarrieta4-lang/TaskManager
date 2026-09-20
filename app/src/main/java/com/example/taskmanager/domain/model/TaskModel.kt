package com.example.taskmanager.domain.model

data class Task(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)