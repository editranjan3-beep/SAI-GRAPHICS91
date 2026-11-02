package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.model.ProjectEntity
import com.example.data.model.RevisionEntity
import com.example.data.repository.ProjectRepository
import com.example.ui.components.getStageIndex
import com.example.ui.viewmodel.WORKFLOW_STAGES
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Sai Graphics", appName)
    }

    @Test
    fun `workflow stages are properly ordered and indexed`() {
        assertEquals(6, WORKFLOW_STAGES.size)
        assertEquals(0, getStageIndex("RAW_INGESTED"))
        assertEquals(2, getStageIndex("COLOR_GRADING"))
        assertEquals(3, getStageIndex("CLIENT_REVIEW"))
        assertEquals(5, getStageIndex("DELIVERED"))
    }

    @Test
    fun `seed database contains initial projects and revisions`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val repo = ProjectRepository(db.projectDao(), db.revisionDao())

        repo.seedInitialDataIfEmpty()
        val projects = repo.allProjects.first()

        assertTrue(projects.isNotEmpty())
        val firstProject = projects.first()
        assertNotNull(firstProject.projectCode)
        assertTrue(firstProject.totalQuotation > 0)
    }
}
