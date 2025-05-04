package data.repository

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.mapper.toDomain
import data.source.project.ProjectDataSource
import data.source.project.ProjectMemoryDataSource
import domain.models.project.Project
import domain.utlis.ProjectExceptions
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class ProjectRepositoryImplTest {
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var repository: ProjectRepositoryImpl
    private lateinit var projectMemoryDataSource: ProjectMemoryDataSource

    @BeforeEach
    fun setup() {
        projectDataSource = mockk(relaxed = true)
        projectMemoryDataSource = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(
            projectDataSource,
            projectMemoryDataSource
        )
    }

    @Test
    fun `getAllProjects returns list of projects when list is not empty `() {
        // Given
        val projects = listOf(
            createProject(id = UUID.randomUUID().toString(), name = "test"),
            createProject(id = UUID.randomUUID().toString(), name = "test2")
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)
        every { projectMemoryDataSource.setProjects(any()) } just Runs
        every { projectMemoryDataSource.getProjects() } returns projects.map { it.toDomain() }

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
        every { projectMemoryDataSource.addProject(project.toDomain()) } returns Unit

        // When
        val result = repository.createProject(project.toDomain())

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteProject returns success when data source return success`() {
        // Given
        val projectId = "26fb5810-951e-4913-aae8-1d36d72d85eb"
        val projects = listOf(
            createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
            createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
        )
        every { projectMemoryDataSource.deleteProject(projectId) } returns projects.map { it.toDomain() }
        every { projectDataSource.getProjects() } returns Result.success(projects)

        // When
        val result = repository.deleteProject(projectId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `editProject returns success when data source return success`() {
        // Given
        val project = createProject(id = UUID.randomUUID().toString(), name = "test")
        val projects = listOf(
            createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
            createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
        )
        every { projectMemoryDataSource.editProject(project.toDomain()) } returns projects.map { it.toDomain() }
        every { projectDataSource.getProjects() } returns Result.success(projects)

        val result = repository.editProject(project.toDomain())

        assertTrue(result.isSuccess)
    }


    @Test
    fun `getProjectLogsById returns project's logs when it exists in projects list`() {

        val projects = listOf(
            createProject(
                id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
                name = "test1",
                projectLogs = listOf("project created", "project updated")
            ),
            createProject(
                id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
                name = "test2",
                projectLogs = listOf("project created", "project updated")
            )
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)
        every { projectMemoryDataSource.getProjects() } returns projects.map { it.toDomain() }


        val result = repository.getProjectLogsById("26fb5810-951e-4913-aae8-1d36d72d85eb")


        assertTrue(result.isSuccess)
    }


    @Test
    fun `getProjectLogsById throw ProjectNotFoundException when project not exists in projects list`() {

        val projects = listOf(
            createProject(
                id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
                name = "test1",
                projectLogs = listOf("project created", "project updated")
            ),
            createProject(
                id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
                name = "test2",
                projectLogs = listOf("project created", "project updated")
            )
        )
        every { projectDataSource.getProjects() } returns Result.success(projects)
        every { projectMemoryDataSource.getProjects() } returns projects.map { it.toDomain() }

        assertThrows<ProjectExceptions.ProjectNotFoundException> {
            val result = repository.getProjectLogsById("5")
            result.getOrThrow()
        }
    }


}