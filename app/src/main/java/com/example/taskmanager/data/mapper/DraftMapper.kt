package com.example.taskmanager.data.mapper

import com.example.taskmanager.data.local.entity.TaskDraftEntity
import com.example.taskmanager.domain.model.TaskDraftModel

fun TaskDraftEntity.toDomain(): TaskDraftModel {
    return TaskDraftModel(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        savedAt = savedAt
    )
}

fun TaskDraftModel.toEntity(): TaskDraftEntity {
    return TaskDraftEntity(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        savedAt = savedAt
    )
}