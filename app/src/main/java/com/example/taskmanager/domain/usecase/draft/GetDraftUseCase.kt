package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class GetDraftsUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {

    suspend operator fun invoke(): Result<List<TaskDraftModel>> {

        return draftRepository.getDrafts()
    }
}