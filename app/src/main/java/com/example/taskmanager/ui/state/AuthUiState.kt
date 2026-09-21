package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.User

sealed interface AuthUiState {

    data object Idle : AuthUiState

    data object Loading : AuthUiState

    data class Success(
        val user: User
    ) : AuthUiState

    data class Error(
        val message: String
    ) : AuthUiState
}