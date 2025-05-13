package data.repository

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.createUserDto
import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectExternalDataSource
import data.source.project.ProjectManager
import data.source.user.CurrentUserProvider
import domain.models.project.Project
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
    private lateinit var currentUserProvider: CurrentUserProvider

    @BeforeEach
    fun setup() {
        projectExternalDataSource = mockk(relaxed = true)
        projectManager = mockk(relaxed = true)
        remoteProjectDataSource = mockk(relaxed = true)
        currentUserProvider = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(
            projectExternalDataSource,
            currentUserProvider
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
    private val projectDto = createProject(
        id = UUID.randomUUID().toString(),
        name = "Test Project",
        description = "Some description",
        createdBy = "user",
        projectLogs = emptyList(),
        projectState = "active",
        taskStates = emptyList(),
        projectStates = emptyList(),
    )

    private val userDto = createUserDto(
        id = UUID.randomUUID().toString(),
        username = "user name",
        passwordHash = "pass",
        role = "role"
    )

    @Test
    fun `getAllProjects returns list of projects when list is not empty `() {
        testScope.runTest {
            coEvery { currentUserProvider.getCurrentUser() } returns userDto
            coEvery { projectExternalDataSource.getProjects(userDto) } returns listOf(projectDto)

            val result = repository.getAllProjects()

            assertNotNull(result)
            assert(result.isNotEmpty())
        }
    }

    @Test
    fun `createProject returns success when data source return success`() {
        testScope.runTest {
            val project = createProject(name = "test")
            coEvery { projectExternalDataSource.createProject(project) } returns Unit
            coEvery { projectManager.addProject(project) } returns Unit

            assertDoesNotThrow {
                repository.createProject(project.toDomain())
            }
        }
    }

    @Test
    fun `deleteProject returns success when data source return success`() {
        testScope.runTest {
            // Given
            val projectId = "26fb5810-951e-4913-aae8-1d36d72d85eb"
            val projects = listOf(
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
            )
            every { projectManager.deleteProject(projectId) } returns projects
            coEvery { projectExternalDataSource.getProjects(mockk()) } returns projects

            // When & Then
            assertDoesNotThrow {
                repository.deleteProject(projectId)
            }
        }
    }

    @Test
    fun `editProject returns success when data source return success`() {
        testScope.runTest {
            // Given
            val project = createProject(id = UUID.randomUUID().toString(), name = "test")
            val projects = listOf(
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test"),
                createProject(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", name = "test2")
            )
            every { projectManager.editProject(project) } returns projects
            coEvery { projectExternalDataSource.getProjects(mockk()) } returns projects


            // When & Then
            assertDoesNotThrow {
                repository.editProject(project.toDomain())

            }
        }
    }


    @Test
    fun `getProjectLogsById returns project's logs when it exists in projects list`() {
        testScope.runTest {
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
            coEvery { remoteProjectDataSource.getProjects(mockk()) } returns projects
            every { projectManager.getProjects() } returns projects

            //When & Then
            assertDoesNotThrow {
                repository.getProjectLogsById("26fb5810-951e-4913-aae8-1d36d72d85eb")
            }
        }
    }


    @Test
    fun `getProjectLogsById throw ProjectNotFoundException when project not exists in projects list`() {
        testScope.runTest {
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
    }

    @Test
    fun `getProjectById should return the correct project when ID matches`() {
        testScope.runTest {
            val id = UUID.randomUUID().toString()
            val expectedProject = createProject(id = id)

            coEvery { projectExternalDataSource.getProjectById(id) } returns expectedProject

            val result = repository.getProjectById(id)

            assertThat(expectedProject).isEqualTo(result.toDto())
        }
    }

    @Test
    fun `getProjectById should throw exception when ID does not match`() {
        testScope.runTest {
            val notExistId = UUID.randomUUID().toString()

            every { projectManager.getProjects() } throws ProjectExceptions.ProjectNotFoundException()

            assertThrows<PlanMateExceptions> { repository.getProjectById(notExistId) }
        }
    }
}
