package com.example.taskmanager.data.remote.api

import com.example.taskmanager.data.remote.dto.TaskDocument
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class TaskApiService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createTask(
        task: TaskDocument
    ): String {

        val document = firestore
            .collection("tasks")
            .document()

        try {

            withTimeout(5000L.milliseconds) {
                document
                    .set(task)
                    .await()
            }

        } catch (e: TimeoutCancellationException) {

            throw Exception("Sin conexión a Internet")
        }

        return document.id
    }

    suspend fun getTasks(
        ownerId: String
    ): Map<String, TaskDocument> {

        val snapshot = firestore
            .collection("tasks")
            .whereEqualTo("ownerId", ownerId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            val task = document.toObject(
                TaskDocument::class.java
            )

            if (task != null) {
                document.id to task
            } else {
                null
            }

        }.toMap()
    }

    suspend fun updateTask(
        taskId: String,
        task: TaskDocument
    ) {

        firestore
            .collection("tasks")
            .document(taskId)
            .set(task)
            .await()
    }

    suspend fun deleteTask(
        taskId: String
    ) {

        firestore
            .collection("tasks")
            .document(taskId)
            .delete()
            .await()
    }
}