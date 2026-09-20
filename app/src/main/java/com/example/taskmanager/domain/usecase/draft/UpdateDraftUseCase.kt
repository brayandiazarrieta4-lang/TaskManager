package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class UpdateDraftUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {

    suspend operator fun invoke(
        draft: TaskDraftModel
    ): Result<TaskDraftModel> {

        return draftRepository.updateDraft(draft)
    }
}