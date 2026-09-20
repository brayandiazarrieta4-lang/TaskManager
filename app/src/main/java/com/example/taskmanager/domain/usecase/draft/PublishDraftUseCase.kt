package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class PublishDraftUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {

    suspend operator fun invoke(
        draft: TaskDraftModel
    ): Result<Task> {

        return draftRepository.publishDraft(draft)
    }
}