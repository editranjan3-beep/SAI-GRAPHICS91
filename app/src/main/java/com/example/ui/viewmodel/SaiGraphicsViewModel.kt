package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.ProjectEntity
import com.example.data.model.RevisionEntity
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class DialogType {
    NONE,
    NEW_PROJECT,
    ADD_REVISION,
    DELIVERY_PASS,
    RECORD_PAYMENT,
    PROJECT_DETAILS
}

val WORKFLOW_STAGES = listOf(
    "RAW_INGESTED" to "1. Raw Ingest",
    "STORY_CUT" to "2. Story Cut",
    "COLOR_GRADING" to "3. Color Grade",
    "CLIENT_REVIEW" to "4. Client Review",
    "MASTER_RENDER" to "5. Master 4K Render",
    "DELIVERED" to "6. Handover & Delivered"
)

class SaiGraphicsViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _selectedProject = MutableStateFlow<ProjectEntity?>(null)
    val selectedProject: StateFlow<ProjectEntity?> = _selectedProject.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Pipeline, 1: Deliveries, 2: Revisions, 3: Lab Hub
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _activeDialog = MutableStateFlow(DialogType.NONE)
    val activeDialog: StateFlow<DialogType> = _activeDialog.asStateFlow()

    val rawProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredProjects: StateFlow<List<ProjectEntity>> = combine(
        rawProjects,
        _searchQuery,
        _selectedFilter
    ) { projects, query, filter ->
        projects.filter { project ->
            val matchesQuery = query.isBlank() ||
                    project.title.contains(query, ignoreCase = true) ||
                    project.clientName.contains(query, ignoreCase = true) ||
                    project.projectCode.contains(query, ignoreCase = true) ||
                    project.location.contains(query, ignoreCase = true) ||
                    project.clientPhone.contains(query)

            val matchesFilter = when (filter) {
                "ALL" -> true
                "REVISIONS" -> project.stage == "CLIENT_REVIEW"
                "DELIVERED" -> project.stage == "DELIVERED"
                "IN_PROGRESS" -> project.stage != "DELIVERED"
                else -> project.eventType.equals(filter, ignoreCase = true)
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentRevisions = MutableStateFlow<List<RevisionEntity>>(emptyList())
    val currentRevisions: StateFlow<List<RevisionEntity>> = _currentRevisions.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun showDialog(dialogType: DialogType) {
        _activeDialog.value = dialogType
    }

    fun dismissDialog() {
        _activeDialog.value = DialogType.NONE
    }

    fun selectProject(project: ProjectEntity?) {
        _selectedProject.value = project
        if (project != null) {
            viewModelScope.launch {
                repository.getRevisionsForProject(project.id).collect { revisions ->
                    _currentRevisions.value = revisions
                }
            }
        } else {
            _currentRevisions.value = emptyList()
        }
    }

    fun clearSelectedProject() {
        selectProject(null)
    }

    fun updateProjectStage(project: ProjectEntity, newStage: String) {
        viewModelScope.launch {
            val updated = project.copy(stage = newStage)
            repository.updateProject(updated)
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = updated
            }
        }
    }

    fun createProject(
        title: String,
        clientName: String,
        clientPhone: String,
        eventType: String,
        eventDate: String,
        location: String,
        deliverables: String,
        rackId: String,
        footageGb: Double,
        quotation: Double,
        advance: Double,
        notes: String
    ) {
        viewModelScope.launch {
            val count = repository.allProjects
            val nextNum = (100..999).random()
            val code = "SG-BHG-$nextNum"
            val passCode = "SG-PASS-${clientName.take(3).uppercase()}-$nextNum"
            val paymentStatus = when {
                advance >= quotation -> "CLEARED"
                advance > 0 -> "PARTIAL"
                else -> "PENDING"
            }

            val newProject = ProjectEntity(
                projectCode = code,
                title = title.ifBlank { "$clientName $eventType" },
                clientName = clientName,
                clientPhone = clientPhone,
                eventType = eventType,
                eventDate = eventDate,
                location = location.ifBlank { "Bhagalpur, Bihar" },
                stage = "RAW_INGESTED",
                storageRackId = rackId.ifBlank { "HDD-01 (Sahmarket Lab)" },
                storageSizeGb = footageGb,
                cloudPreviewUrl = "https://drive.google.com/drive/folders/sai-graphics-preview-$nextNum",
                finalDeliveryUrl = "https://drive.google.com/drive/folders/sai-graphics-master-$nextNum",
                deliveryPassCode = passCode,
                deliverablesSummary = deliverables,
                totalQuotation = quotation,
                advancePaid = advance,
                paymentStatus = paymentStatus,
                leadEditor = "Sai Graphics Lab",
                notes = notes
            )
            val newId = repository.insertProject(newProject)
            _selectedProject.value = newProject.copy(id = newId)
            dismissDialog()
        }
    }

    fun addRevision(
        versionTag: String,
        timecodeOrSheet: String,
        category: String,
        feedbackNote: String
    ) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            val revision = RevisionEntity(
                projectId = current.id,
                versionTag = versionTag.ifBlank { "v1.0" },
                timecodeOrSheet = timecodeOrSheet.ifBlank { "Timeline" },
                category = category,
                feedbackNote = feedbackNote,
                status = "PENDING"
            )
            repository.insertRevision(revision)
            // also set project stage to CLIENT_REVIEW if it wasn't
            if (current.stage == "COLOR_GRADING" || current.stage == "STORY_CUT") {
                updateProjectStage(current, "CLIENT_REVIEW")
            }
            dismissDialog()
        }
    }

    fun toggleRevisionStatus(revision: RevisionEntity) {
        viewModelScope.launch {
            val nextStatus = when (revision.status) {
                "PENDING" -> "IN_PROGRESS"
                "IN_PROGRESS" -> "RESOLVED"
                else -> "PENDING"
            }
            repository.updateRevisionStatus(revision.id, nextStatus)
        }
    }

    fun deleteRevision(revision: RevisionEntity) {
        viewModelScope.launch {
            repository.deleteRevision(revision)
        }
    }

    fun recordPayment(project: ProjectEntity, additionalAmount: Double) {
        viewModelScope.launch {
            val newAdvance = (project.advancePaid + additionalAmount).coerceAtMost(project.totalQuotation)
            val newStatus = if (newAdvance >= project.totalQuotation) "CLEARED" else "PARTIAL"
            val updated = project.copy(advancePaid = newAdvance, paymentStatus = newStatus)
            repository.updateProject(updated)
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = updated
            }
            dismissDialog()
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = null
            }
        }
    }

    // Quick Action Intents
    fun dialPhone(context: Context, phone: String = "8809216991") {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open dialer: $phone", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phone: String, message: String) {
        try {
            val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
            val internationalPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
            val url = "https://wa.me/$internationalPhone?text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed. Opening browser...", Toast.LENGTH_SHORT).show()
        }
    }

    fun openStudioMap(context: Context) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=Sahmarket+Picture+Palace+Bhagalpur+Bihar")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Location: Sahmarket Picture Palace, Bhagalpur", Toast.LENGTH_LONG).show()
        }
    }

    fun openUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open link: $url", Toast.LENGTH_SHORT).show()
        }
    }

    fun generateDeliveryMessage(project: ProjectEntity): String {
        val balance = (project.totalQuotation - project.advancePaid).coerceAtLeast(0.0)
        return """
🎬 *SAI GRAPHICS POST PRODUCTION - BHAGALPUR*
📍 Sahmarket Picture Palace, Bhagalpur, Bihar
📞 Editing Lab: 8809216991

Dear *${project.clientName}*,
Your official post-production deliverables for *${project.title}* are finalized and ready for handover!

📋 *Project Code:* ${project.projectCode}
📅 *Event Date:* ${project.eventDate} (${project.location})
🎯 *Deliverables:*
${project.deliverablesSummary}

🔐 *Access PIN:* ${project.deliveryPassCode}
💾 *Master Cloud Download:* ${project.finalDeliveryUrl}
👁️ *Preview Stream:* ${project.cloudPreviewUrl}

💰 *Payment Status:* ${project.paymentStatus} ${if (balance > 0) "(Balance Due: ₹${balance.toLong()})" else "(Fully Cleared)"}

Thank you for trusting Sai Graphics Post Production for your memorable moments!
        """.trimIndent()
    }
}

class SaiGraphicsViewModelFactory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaiGraphicsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaiGraphicsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
