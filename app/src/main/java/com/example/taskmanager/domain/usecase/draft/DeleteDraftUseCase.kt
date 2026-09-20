package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class DeleteDraftUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {

    suspend operator fun invoke(
        draftId: Long
    ): Result<Unit> {

        return draftRepository.deleteDraft(draftId)
    }
}