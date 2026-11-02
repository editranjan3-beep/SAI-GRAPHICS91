package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ProjectEntity
import com.example.ui.viewmodel.DialogType
import com.example.ui.components.ProjectCard
import com.example.ui.components.StudioHeader
import com.example.ui.components.outlinedFieldColors
import com.example.ui.theme.CinemaAmber
import com.example.ui.viewmodel.SaiGraphicsViewModel

private val FILTER_OPTIONS = listOf(
    "ALL" to "All Projects",
    "Wedding Cinematography" to "Weddings",
    "Candid Photography" to "Candid Photos",
    "Pre-Wedding Teaser" to "Pre-Wedding",
    "Commercial Event" to "Commercial",
    "REVISIONS" to "Under Revision",
    "DELIVERED" to "Delivered"
)

@Composable
fun ProjectsScreen(
    viewModel: SaiGraphicsViewModel,
    onProjectClick: (ProjectEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.filteredProjects.collectAsStateWithLifecycle()
    val allProjects by viewModel.rawProjects.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    val totalProjects = allProjects.size
    val activeRevisions = allProjects.count { it.stage == "CLIENT_REVIEW" }
    val readyCount = allProjects.count { it.stage == "MASTER_RENDER" || it.stage == "DELIVERED" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("projects_list_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Header with Hero Banner & Stats
        item {
            StudioHeader(
                viewModel = viewModel,
                totalProjects = totalProjects,
                activeRevisions = activeRevisions,
                readyCount = readyCount,
                onNewProjectClick = { viewModel.showDialog(DialogType.NEW_PROJECT) }
            )
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search client, wedding, project code or phone...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CinemaAmber
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_projects_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors()
            )
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FILTER_OPTIONS.forEach { (key, label) ->
                    val isSelected = selectedFilter == key
                    Surface(
                        onClick = { viewModel.setSelectedFilter(key) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) CinemaAmber else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("filter_chip_$key")
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Projects List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "POST-PRODUCTION WORKFLOW (${projects.size})",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Bhagalpur Suite",
                    style = MaterialTheme.typography.labelSmall,
                    color = CinemaAmber,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Project Cards
        if (projects.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = CinemaAmber,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching projects found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing your search query or create a new wedding cinematography project.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(projects, key = { it.id }) { project ->
                ProjectCard(
                    project = project,
                    viewModel = viewModel,
                    onClick = { onProjectClick(project) },
                    onDeliveryClick = {
                        viewModel.selectProject(project)
                        viewModel.showDialog(DialogType.DELIVERY_PASS)
                    }
                )
            }
        }
    }
}
