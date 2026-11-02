package com.example.data.repository

import com.example.data.dao.ProjectDao
import com.example.data.dao.RevisionDao
import com.example.data.model.ProjectEntity
import com.example.data.model.RevisionEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val revisionDao: RevisionDao
) {
    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val pendingRevisionsCount: Flow<Int> = revisionDao.getPendingRevisionsCount()

    fun getProjectById(id: Long): Flow<ProjectEntity?> = projectDao.getProjectById(id)

    fun getRevisionsForProject(projectId: Long): Flow<List<RevisionEntity>> =
        revisionDao.getRevisionsForProject(projectId)

    suspend fun insertProject(project: ProjectEntity): Long = projectDao.insertProject(project)

    suspend fun updateProject(project: ProjectEntity) = projectDao.updateProject(project)

    suspend fun deleteProject(project: ProjectEntity) = projectDao.deleteProject(project)

    suspend fun insertRevision(revision: RevisionEntity): Long = revisionDao.insertRevision(revision)

    suspend fun updateRevision(revision: RevisionEntity) = revisionDao.updateRevision(revision)

    suspend fun updateRevisionStatus(id: Long, status: String) =
        revisionDao.updateRevisionStatus(id, status)

    suspend fun deleteRevision(revision: RevisionEntity) = revisionDao.deleteRevision(revision)

    suspend fun seedInitialDataIfEmpty() {
        if (projectDao.getProjectCount() == 0) {
            val p1Id = projectDao.insertProject(
                ProjectEntity(
                    projectCode = "SG-BHG-2401",
                    title = "Vikram & Sneha Grand Wedding Film",
                    clientName = "Vikram Keshri",
                    clientPhone = "9835124098",
                    clientEmail = "vikram.keshri@gmail.com",
                    eventType = "Wedding Cinematography",
                    eventDate = "18 Feb 2026",
                    location = "Hotel Rajhans, Bhagalpur",
                    stage = "CLIENT_REVIEW",
                    storageRackId = "HDD-04 (Suite Bay 1)",
                    storageSizeGb = 420.0,
                    cloudPreviewUrl = "https://drive.google.com/drive/folders/sai-graphics-preview-sample",
                    finalDeliveryUrl = "https://drive.google.com/drive/folders/sai-graphics-master-deliveries",
                    deliveryPassCode = "SG-8809-VKS",
                    deliverablesSummary = "4K Cinematic Teaser (4m) + Full Traditional Film (2.5h) + 40-Sheet Candid Album + 6 IG Reels",
                    totalQuotation = 65000.0,
                    advancePaid = 40000.0,
                    paymentStatus = "PARTIAL",
                    leadEditor = "Ranjan Kumar (Lead Colorist)",
                    notes = "Sony FX3 + A7IV multi-cam multicam footage. High dynamic range Haldi and Varmala."
                )
            )

            // Add sample revisions for project 1
            revisionDao.insertRevision(
                RevisionEntity(
                    projectId = p1Id,
                    versionTag = "v1.1",
                    timecodeOrSheet = "03:42 (Varmala slow-mo)",
                    category = "SONG_AUDIO",
                    feedbackNote = "Change background song from Shehnai to modern 'Kudmayi' flute instrumental mix.",
                    status = "IN_PROGRESS"
                )
            )
            revisionDao.insertRevision(
                RevisionEntity(
                    projectId = p1Id,
                    versionTag = "v1.2",
                    timecodeOrSheet = "Sheet #22 (Groom Haldi)",
                    category = "COLOR_GRADE",
                    feedbackNote = "Warm up yellow hues and lower contrast on outdoor midday lawn shots.",
                    status = "PENDING"
                )
            )

            val p2Id = projectDao.insertProject(
                ProjectEntity(
                    projectCode = "SG-BHG-2402",
                    title = "Ananya & Rohan Pre-Wedding Cinematic Teaser",
                    clientName = "Ananya Sharma",
                    clientPhone = "9431876521",
                    clientEmail = "ananya.sharma@outlook.com",
                    eventType = "Pre-Wedding Teaser",
                    eventDate = "05 Mar 2026",
                    location = "Vikramshila Ruins & Ganga Ghats, Kahalgaon",
                    stage = "COLOR_GRADING",
                    storageRackId = "HDD-07 (Bay 2)",
                    storageSizeGb = 185.0,
                    cloudPreviewUrl = "https://drive.google.com/drive/folders/sai-graphics-preview-sample",
                    finalDeliveryUrl = "https://drive.google.com/drive/folders/sai-graphics-master-deliveries",
                    deliveryPassCode = "SG-8809-ARN",
                    deliverablesSummary = "3-Minute 4K Cinematic Teaser + 30 Edited Candid Portraits + 4 Vertical Reels",
                    totalQuotation = 35000.0,
                    advancePaid = 20000.0,
                    paymentStatus = "PARTIAL",
                    leadEditor = "Sai Graphics Post Lab",
                    notes = "Sunset drone footage over Ganges. Film LUT applied: Kodak 2383 emulation."
                )
            )

            revisionDao.insertRevision(
                RevisionEntity(
                    projectId = p2Id,
                    versionTag = "v1.0",
                    timecodeOrSheet = "01:15 (Boat Sequence)",
                    category = "PACING_CUT",
                    feedbackNote = "Extend drone fly-over shot across the river by 3 seconds.",
                    status = "RESOLVED"
                )
            )

            projectDao.insertProject(
                ProjectEntity(
                    projectCode = "SG-BHG-2403",
                    title = "Bhagalpur Silk Heritage Commercial",
                    clientName = "Anurag Silk Mills",
                    clientPhone = "8809216991",
                    clientEmail = "anuragsilk.bhagalpur@gmail.com",
                    eventType = "Commercial Event",
                    eventDate = "10 Jan 2026",
                    location = "Sahmarket Commercial Lab, Bhagalpur",
                    stage = "MASTER_RENDER",
                    storageRackId = "NVMe-SSD-02 (Fast Cache)",
                    storageSizeGb = 95.0,
                    cloudPreviewUrl = "https://drive.google.com/drive/folders/sai-graphics-preview-sample",
                    finalDeliveryUrl = "https://drive.google.com/drive/folders/sai-graphics-master-deliveries",
                    deliveryPassCode = "SG-8809-ASM",
                    deliverablesSummary = "60s Brand Film + 30s TV Broadcast Cut + 15s Instagram Ads with Hindi Voiceover",
                    totalQuotation = 45000.0,
                    advancePaid = 45000.0,
                    paymentStatus = "CLEARED",
                    leadEditor = "Ranjan Kumar",
                    notes = "Color accuracy calibrated for silk fabric weave details. 10-bit ProRes master."
                )
            )

            projectDao.insertProject(
                ProjectEntity(
                    projectCode = "SG-BHG-2404",
                    title = "Pooja & Amit Royal Wedding Album",
                    clientName = "Amit Choudhary",
                    clientPhone = "9123456780",
                    clientEmail = "amit.choudhary@yahoo.com",
                    eventType = "Candid Photography",
                    eventDate = "14 Dec 2025",
                    location = "Tilkamanjhi, Bhagalpur",
                    stage = "DELIVERED",
                    storageRackId = "Archive-Cold-01",
                    storageSizeGb = 310.0,
                    cloudPreviewUrl = "https://drive.google.com/drive/folders/sai-graphics-preview-sample",
                    finalDeliveryUrl = "https://drive.google.com/drive/folders/sai-graphics-master-deliveries",
                    deliveryPassCode = "SG-8809-PAC",
                    deliverablesSummary = "50-Sheet Velvet Touch Karigari Album + USB Master Key + 800 Retouched Candids",
                    totalQuotation = 50000.0,
                    advancePaid = 50000.0,
                    paymentStatus = "CLEARED",
                    leadEditor = "Sai Graphics Lab",
                    notes = "Delivered to client in Bhagalpur. Master cloud backup active for 1 year."
                )
            )
        }
    }
}
