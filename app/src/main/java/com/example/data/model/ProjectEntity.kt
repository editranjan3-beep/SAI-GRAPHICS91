package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectCode: String,
    val title: String,
    val clientName: String,
    val clientPhone: String,
    val clientEmail: String = "",
    val eventType: String, // Wedding Cinematography, Candid Photography, Pre-Wedding Teaser, Commercial Event, Album Designing
    val eventDate: String,
    val location: String, // e.g. "Hotel Rajhans, Bhagalpur"
    val stage: String, // "RAW_INGESTED", "STORY_CUT", "COLOR_GRADING", "CLIENT_REVIEW", "MASTER_RENDER", "DELIVERED"
    val storageRackId: String, // e.g. "HDD-08 (Lab Rack A)"
    val storageSizeGb: Double, // e.g. 240.0
    val cloudPreviewUrl: String, // Google Drive / Cloud review stream
    val finalDeliveryUrl: String, // Master export folder
    val deliveryPassCode: String, // PIN for client verification
    val deliverablesSummary: String, // "4K Cinematic Teaser + 3h Full Traditional Film + 40-Sheet Album + 5 Reels"
    val totalQuotation: Double,
    val advancePaid: Double,
    val paymentStatus: String, // "PENDING", "PARTIAL", "CLEARED"
    val leadEditor: String = "Sai Graphics Lab",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
