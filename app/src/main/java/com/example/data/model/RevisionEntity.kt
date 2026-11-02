package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revisions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["projectId"])]
)
data class RevisionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val versionTag: String, // e.g. "v1.1", "v2.0", "vFinal"
    val timecodeOrSheet: String, // e.g. "04:32 (Varmala cut)" or "Sheet #16"
    val category: String, // "SONG_AUDIO", "COLOR_GRADE", "PACING_CUT", "PHOTO_SWAP", "SPELLING_TITLES", "OTHER"
    val feedbackNote: String,
    val status: String = "PENDING", // "PENDING", "IN_PROGRESS", "RESOLVED"
    val requestedAt: Long = System.currentTimeMillis()
)
