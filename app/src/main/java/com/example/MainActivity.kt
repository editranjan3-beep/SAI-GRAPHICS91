package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.repository.ProjectRepository
import com.example.ui.components.AddRevisionDialog
import com.example.ui.components.DeliveryPassDialog
import com.example.ui.components.NewProjectDialog
import com.example.ui.components.RecordPaymentDialog
import com.example.ui.screens.DeliveriesScreen
import com.example.ui.screens.ProjectDetailScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.RevisionsBoardScreen
import com.example.ui.screens.StudioHubScreen
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.GradingTeal
import com.example.ui.theme.SaiGraphicsTheme
import com.example.ui.viewmodel.DialogType
import com.example.ui.viewmodel.SaiGraphicsViewModel
import com.example.ui.viewmodel.SaiGraphicsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SaiGraphicsTheme(darkTheme = true) {
                SaiGraphicsApp()
            }
        }
    }
}

@Composable
fun SaiGraphicsApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { ProjectRepository(database.projectDao(), database.revisionDao()) }
    val viewModel: SaiGraphicsViewModel = viewModel(
        factory = SaiGraphicsViewModelFactory(repository)
    )

    val selectedProject by viewModel.selectedProject.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeDialog by viewModel.activeDialog.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedProject != null) {
            ProjectDetailScreen(
                project = selectedProject!!,
                viewModel = viewModel,
                onBack = { viewModel.clearSelectedProject() }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { viewModel.setSelectedTab(0) },
                            icon = { Icon(Icons.Default.Movie, contentDescription = "Pipeline") },
                            label = { Text("Pipeline", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                            colors = navItemColors(),
                            modifier = Modifier.testTag("nav_tab_pipeline")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { viewModel.setSelectedTab(1) },
                            icon = { Icon(Icons.Default.Send, contentDescription = "Deliveries") },
                            label = { Text("Deliveries", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                            colors = navItemColors(),
                            modifier = Modifier.testTag("nav_tab_deliveries")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { viewModel.setSelectedTab(2) },
                            icon = { Icon(Icons.Default.EditNote, contentDescription = "Revisions") },
                            label = { Text("Revisions", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                            colors = navItemColors(),
                            modifier = Modifier.testTag("nav_tab_revisions")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { viewModel.setSelectedTab(3) },
                            icon = { Icon(Icons.Default.Business, contentDescription = "Studio") },
                            label = { Text("Studio Hub", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                            colors = navItemColors(),
                            modifier = Modifier.testTag("nav_tab_studio")
                        )
                    }
                },
                floatingActionButton = {
                    if (selectedTab == 0) {
                        FloatingActionButton(
                            onClick = { viewModel.showDialog(DialogType.NEW_PROJECT) },
                            containerColor = CinemaAmber,
                            contentColor = Color.Black,
                            modifier = Modifier.testTag("main_fab_new_project")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New Project")
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedTab) {
                        0 -> ProjectsScreen(
                            viewModel = viewModel,
                            onProjectClick = { viewModel.selectProject(it) }
                        )
                        1 -> DeliveriesScreen(
                            viewModel = viewModel,
                            onSelectProject = { viewModel.selectProject(it) }
                        )
                        2 -> RevisionsBoardScreen(
                            viewModel = viewModel,
                            onSelectProject = { viewModel.selectProject(it) }
                        )
                        3 -> StudioHubScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        // Dialog Overlays
        when (activeDialog) {
            DialogType.NEW_PROJECT -> {
                NewProjectDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            DialogType.ADD_REVISION -> {
                AddRevisionDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            DialogType.DELIVERY_PASS -> {
                selectedProject?.let { project ->
                    DeliveryPassDialog(
                        project = project,
                        viewModel = viewModel,
                        onDismiss = { viewModel.dismissDialog() }
                    )
                }
            }
            DialogType.RECORD_PAYMENT -> {
                selectedProject?.let { project ->
                    RecordPaymentDialog(
                        project = project,
                        viewModel = viewModel,
                        onDismiss = { viewModel.dismissDialog() }
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = CinemaAmber,
    selectedTextColor = CinemaAmber,
    indicatorColor = CinemaAmber.copy(alpha = 0.15f),
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)
