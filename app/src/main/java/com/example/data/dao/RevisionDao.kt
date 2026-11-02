package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RevisionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RevisionDao {
    @Query("SELECT * FROM revisions WHERE projectId = :projectId ORDER BY requestedAt DESC")
    fun getRevisionsForProject(projectId: Long): Flow<List<RevisionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevision(revision: RevisionEntity): Long

    @Update
    suspend fun updateRevision(revision: RevisionEntity)

    @Delete
    suspend fun deleteRevision(revision: RevisionEntity)

    @Query("UPDATE revisions SET status = :status WHERE id = :id")
    suspend fun updateRevisionStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM revisions WHERE status != 'RESOLVED'")
    fun getPendingRevisionsCount(): Flow<Int>
}
