package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.Task

interface TaskRepository {

    suspend fun createTask(task: Task): Result<Task>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun updateTask(task: Task): Result<Task>

    suspend fun deleteTask(taskId: String): Result<Unit>
}