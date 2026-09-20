package com.example.taskmanager.domain.usecase.task

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(): Result<List<Task>> {
        return taskRepository.getTasks()
    }
}