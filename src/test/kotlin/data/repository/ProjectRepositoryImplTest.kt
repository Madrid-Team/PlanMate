package data.repository

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectExternalDataSource
import data.source.project.ProjectManager
import domain.models.project.Project
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertNotNull

class ProjectRepositoryImplTest {
    private lateinit var projectExternalDataSource: ProjectExternalDataSource
    private lateinit var repository: ProjectRepositoryImpl
    private lateinit var projectManager: ProjectManager
    private lateinit var remoteProjectDataSource: ProjectExternalDataSource
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        projectExternalDataSource = mockk(relaxed = true)
        projectManager = mockk(relaxed = true)
        remoteProjectDataSource = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(
            projectExternalDataSource,
        )
        testScope = TestScope()
    }
    val project = Project(
        id = UUID.randomUUID(),
        name = "Test Project",
        description = "Some description",
        createdBy = "user",
        projectLogs = emptyList(),
        projectState = "active",
        taskStates = emptyList(),
        projectStates = emptyList(),

    )
    @Test
    fun `getAllProjects returns list of projects when list is not empty `() {
        testScope.runTest {

            // Given
            repository.createProject(project)


            every { projectManager.setProjects(any()) } just Runs


            // When
            val result = repository.getAllProjects()

            // Then
            assertNotNull(result)
        }
    }

    @Test
    fun `createProject returns success when data source return success`() {
        testScope.runTest {
            // Given
            val project = createProject(name = "test")
            coEvery { projectExternalDataSource.createProject(project) } returns Unit
            coEvery { projectManager.addProject(project) } returns Unit

            // When
            // Then
            assertDoesNotThrow {
                repository.createProject(project.toDomain())
            }
        }
    }

    @Test
    fun `deleteProject returns success when data source return success`() {
        runTest {
            // Given
            val projectId = "26fb5810-951e-4913-aae8-1d36d72d85eb"
            val projects = listOf(
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
            )
            every { projectManager.deleteProject(projectId) } returns projects
            coEvery { projectExternalDataSource.getProjects() } returns projects

            // When & Then
            assertDoesNotThrow {
                repository.deleteProject(projectId)
            }
        }
    }

    @Test
    fun `editProject returns success when data source return success`() {
        runTest {
            // Given
            val project = createProject(id = UUID.randomUUID().toString(), name = "test")
            val projects = listOf(
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
            )
            every { projectManager.editProject(project) } returns projects
            coEvery { projectExternalDataSource.getProjects() } returns projects


            // When & Then
            assertDoesNotThrow {
                repository.editProject(project.toDomain())

            }
        }
    }


    @Test
    fun `getProjectLogsById returns project's logs when it exists in projects list`() {
        runTest {
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
            coEvery { remoteProjectDataSource.getProjects() } returns projects
            every { projectManager.getProjects() } returns projects

            //When & Then
            assertDoesNotThrow {
                repository.getProjectLogsById("26fb5810-951e-4913-aae8-1d36d72d85eb")
            }
        }
    }


    @Test
    fun `getProjectLogsById throw ProjectNotFoundException when project not exists in projects list`() {
        runTest {
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
            every { projectManager.getProjects() } returns projects

            assertThrows<PlanMateExceptions> {
                repository.getProjectById("5")
            }
        }

        @Test
        fun `getProjectById should return the correct project when ID matches`() {
            runTest {
                val id = UUID.randomUUID()
                val expectedProject = createProject(id = id.toString())

                every { projectManager.getProjects() } returns listOf(expectedProject)

                val result = repository.getProjectById(id.toString())

                assertThat(expectedProject).isEqualTo(result.toDto())
            }
        }

        @Test
        fun `getProjectById should throw exception when ID does not match`() {
            runTest {
                val notExistId = UUID.randomUUID().toString()

                every { projectManager.getProjects() } throws ProjectExceptions.ProjectNotFoundException()

                assertThrows<ProjectExceptions.ProjectNotFoundException> { repository.getProjectById(notExistId) }
            }
        }
    }

}