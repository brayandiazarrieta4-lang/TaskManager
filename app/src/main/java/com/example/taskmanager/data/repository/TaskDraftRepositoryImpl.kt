package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.DraftDao
import com.example.taskmanager.data.mapper.toDomain
import com.example.taskmanager.data.mapper.toEntity
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.domain.repository.DraftRepository
import com.example.taskmanager.domain.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class DraftRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao,
    private val firebaseAuth: FirebaseAuth,
    private val taskRepository: TaskRepository
) : DraftRepository {
    override suspend fun saveDraft(
        draft: TaskDraftModel
    ): Result<TaskDraftModel> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val draftWithOwner = draft.copy(
                ownerId = currentUser.uid
            )

            val id = draftDao.insertDraft(
                draftWithOwner.toEntity()
            )

            Result.success(
                draftWithOwner.copy(
                    id = id
                )
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun getDrafts(): Result<List<TaskDraftModel>> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val drafts = draftDao.getDrafts(
                currentUser.uid
            )

            Result.success(
                drafts.map { it.toDomain() }
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateDraft(
        draft: TaskDraftModel
    ): Result<TaskDraftModel> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val existingDraft = draftDao.getDraft(
                draft.id,
                currentUser.uid
            ) ?: return Result.failure(
                Exception("Borrador no encontrado")
            )

            val updatedDraft = draft.copy(
                ownerId = currentUser.uid
            )

            draftDao.updateDraft(
                updatedDraft.toEntity()
            )

            Result.success(updatedDraft)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun deleteDraft(
        draftId: Long
    ): Result<Unit> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val drafts = draftDao.getDrafts(
                currentUser.uid
            )

            val draft = drafts.find {
                it.id == draftId
            }

            if (draft == null) {
                return Result.failure(
                    Exception("Borrador no encontrado")
                )
            }

            draftDao.deleteDraft(draft)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun publishDraft(
        draft: TaskDraftModel
    ): Result<Task> {

        return try {

            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(
                    Exception("No hay un usuario autenticado")
                )

            val task = Task(
                id = "",
                ownerId = currentUser.uid,
                title = draft.title,
                description = draft.description,
                completed = false,
                createdAt = draft.savedAt,
                updatedAt = System.currentTimeMillis()
            )

            val result = taskRepository.createTask(task)

            if (result.isSuccess) {

                val drafts = draftDao.getDrafts(
                    currentUser.uid
                )

                val draftEntity = drafts.find {
                    it.id == draft.id
                }

                if (draftEntity != null) {
                    draftDao.deleteDraft(draftEntity)
                }

                Result.success(
                    result.getOrThrow()
                )

            } else {

                Result.failure(
                    result.exceptionOrNull()
                        ?: Exception("No se pudo publicar el borrador")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
