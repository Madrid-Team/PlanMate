package data.repository

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.source.project.ProjectDataSource
import domain.mapper.toDomain
import domain.utlis.ProjectNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        every { projectDataSource.getProjects() } returns Result.success(projects)

        repository = ProjectRepositoryImpl(projectDataSource)

        // When
        val result = repository.getAllProjects().getOrNull()

        // Then
        assertThat(result).containsExactlyElementsIn(projects.map { it.toDomain() })
    }

    @Test
    fun `createProject returns success when data source return success`() {
        // Given
        val project = createProject(name = "test")
        every { projectDataSource.createProject(project) } returns Result.success(Unit)

        // When
        val result = repository.createProject(project.toDomain())

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteProject returns success when data source return success`() {
        // Given
        val projectId = "1"
        val projects = listOf(
            createProject(id = "1", name = "test"),
            createProject(id = "2", name = "test2")
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)

        repository = ProjectRepositoryImpl(projectDataSource)

        // When
        val result = repository.deleteProject(projectId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `editProject returns success when data source return success`() {
        // Given
        val project = createProject(id = "1", name = "test")
        val projects = listOf(
            createProject(id = "1", name = "test"),
            createProject(id = "2", name = "test2")
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)
        repository = ProjectRepositoryImpl(projectDataSource)

        val result = repository.editProject(project.toDomain())

        assertTrue(result.isSuccess)
    }


    @Test
    fun `getProjectLogsById returns project's logs when it exists in projects list`() {

        val projects = listOf(
            createProject(id = "1", name = "test1", projectLogs = listOf("project created", "project updated")),
            createProject(id = "2", name = "test2", projectLogs = listOf("project created", "project updated"))
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)

        repository = ProjectRepositoryImpl(projectDataSource)

        val result = repository.getProjectLogsById("1")


        assertTrue(result.isSuccess)
    }


    @Test
    fun `getProjectLogsById throw ProjectNotFoundException when project not exists in projects list`() {

        val projects = listOf(
            createProject(id = "1", name = "test1", projectLogs = listOf("project created", "project updated")),
            createProject(id = "2", name = "test2", projectLogs = listOf("project created", "project updated"))
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)

        repository = ProjectRepositoryImpl(projectDataSource)

        assertThrows<ProjectNotFoundException> {
            val result = repository.getProjectLogsById("5")
            result.getOrThrow()
        }
    }


}