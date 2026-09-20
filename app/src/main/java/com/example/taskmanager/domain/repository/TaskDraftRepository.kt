package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.model.TaskDraftModel

interface DraftRepository {

    suspend fun saveDraft(draft: TaskDraftModel): Result<TaskDraftModel>

    suspend fun getDrafts(): Result<List<TaskDraftModel>>

    suspend fun updateDraft(draft: TaskDraftModel): Result<TaskDraftModel>

    suspend fun deleteDraft(draftId: Long): Result<Unit>

    suspend fun publishDraft(draft: TaskDraftModel): Result<Task>
}