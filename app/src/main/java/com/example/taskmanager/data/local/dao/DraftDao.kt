package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.local.entity.TaskDraftEntity

@Dao
interface DraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(
        draft: TaskDraftEntity
    ): Long

    @Query(
        "SELECT * FROM task_drafts WHERE ownerId = :ownerId"
    )
    suspend fun getDrafts(
        ownerId: String
    ): List<TaskDraftEntity>

    @Query(
        """
        SELECT * FROM task_drafts
        WHERE id = :draftId
        AND ownerId = :ownerId
        LIMIT 1
        """
    )
    suspend fun getDraft(
        draftId: Long,
        ownerId: String
    ): TaskDraftEntity?

    @Update
    suspend fun updateDraft(
        draft: TaskDraftEntity
    )

    @Delete
    suspend fun deleteDraft(
        draft: TaskDraftEntity
    )
}