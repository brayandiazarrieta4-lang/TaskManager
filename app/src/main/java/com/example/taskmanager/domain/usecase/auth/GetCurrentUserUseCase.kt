package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): User? {

        return authRepository.getCurrentUser()
    }
}