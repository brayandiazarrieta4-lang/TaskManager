package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.Task

sealed interface TaskUiState {

    data object Loading : TaskUiState

    data class Success(
        val tasks: List<Task>
    ) : TaskUiState

    data object Empty : TaskUiState

    data class Error(
        val message: String
    ) : TaskUiState
}