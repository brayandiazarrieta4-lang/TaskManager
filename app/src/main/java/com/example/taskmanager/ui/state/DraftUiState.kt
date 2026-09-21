package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.TaskDraftModel

sealed interface DraftUiState {

    data object Loading : DraftUiState

    data class Success(
        val drafts: List<TaskDraftModel>
    ) : DraftUiState

    data object Empty : DraftUiState

    data class Error(
        val message: String
    ) : DraftUiState
}