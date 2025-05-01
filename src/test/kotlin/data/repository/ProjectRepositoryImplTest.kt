package data.repository

import data.createProject
import data.source.project.ProjectDataSource
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectRepositoryImplTest {
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var repository: ProjectRepositoryImpl

    @BeforeEach
    fun setup() {
        projectDataSource = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(projectDataSource)
    }

        @Test
    fun `getAllProjects returns list of projects when list is not empty `() {
        // Given
        val projects = listOf(
            createProject(id = "1", name = "test"),
            createProject(id = "2", name = "test2")
        )
        every { projectDataSource.getAllProjects() } returns projects

        // When
        val result = repository.getAllProjects()

        // Then
        assertEquals(projects, result)
    }

    @Test
    fun `createProject returns success when data source return success`() {
        // Given
        val project = createProject(name = "test")
        every { projectDataSource.createProject(project) } returns Result.success(Unit)

        // When
        val result = repository.createProject(project)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteProject returns success when data source return success`() {
        // Given
        val projectId = "1"
        every { projectDataSource.deleteProject(projectId) } returns Result.success(Unit)

        // When
        val result = repository.deleteProject(projectId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `editProject returns success when data source return success`() {

        val updatedProject = createProject(id = "1", name = "Updated Name")
        every { projectDataSource.editProject(updatedProject) } returns Result.success(Unit)

        val result = repository.editProject(updatedProject)

        assertTrue(result.isSuccess)
    }

}