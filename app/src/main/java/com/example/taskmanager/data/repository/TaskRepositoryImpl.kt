package com.example.taskmanager.data.repository

import com.example.taskmanager.data.mapper.toDocument
import com.example.taskmanager.data.mapper.toDomain
import com.example.taskmanager.data.remote.api.TaskApiService
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApiService,
    private val firebaseAuth: FirebaseAuth
) : TaskRepository {

    override suspend fun createTask(
        task: Task
    ): Result<Task> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val taskWithOwner = task.copy(
                ownerId = currentUser.uid
            )

            val taskDocument = taskWithOwner.toDocument()

            val documentId = taskApi.createTask(
                taskDocument
            )

            Result.success(
                taskWithOwner.copy(
                    id = documentId
                )
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val documents = taskApi.getTasks(
                currentUser.uid
            )

            val tasks = documents.map { (id, document) ->
                document.toDomain(id)
            }

            Result.success(tasks)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateTask(
        task: Task
    ): Result<Task> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val taskWithOwner = task.copy(
                ownerId = currentUser.uid
            )

            val taskDocument = taskWithOwner.toDocument()

            taskApi.updateTask(
                task.id,
                taskDocument
            )

            Result.success(taskWithOwner)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun deleteTask(
        taskId: String
    ): Result<Unit> {

        return try {

            firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            taskApi.deleteTask(taskId)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}