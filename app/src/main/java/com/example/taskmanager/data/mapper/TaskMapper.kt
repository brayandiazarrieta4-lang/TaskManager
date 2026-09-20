package com.example.taskmanager.data.mapper

import com.example.taskmanager.data.remote.dto.TaskDocument
import com.example.taskmanager.domain.model.Task

fun TaskDocument.toDomain(id: String): Task {
    return Task(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toDocument(): TaskDocument {
    return TaskDocument(
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}