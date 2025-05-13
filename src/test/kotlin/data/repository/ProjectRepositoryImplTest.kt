package data.repository

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.createUserDto
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
    }

    @Test
    fun `getAllProjects returns list of projects when list is not empty `() {
        runTest {
            coEvery { currentUserProvider.getCurrentUser() } returns userDto
            coEvery { projectExternalDataSource.getProjects(userDto) } returns listOf(projectDto)

            val result = repository.getAllProjects()

            assertNotNull(result)
            assert(result.isNotEmpty())
        }
    }

    @Test
    fun `createProject should create project successfully when data source create it successfully`() {
        runTest {
            coEvery { projectExternalDataSource.createProject(projectDto) } returns Unit
            coEvery { projectManager.addProject(projectDto) } returns Unit

            assertDoesNotThrow {
                repository.createProject(project)
            }
        }
    }

    @Test
    fun `deleteProject should delete project successfully when data source delete it successfully`() {
        runTest {
            every { projectManager.deleteProject(project.id.toString()) } returns listOf(projectDto)
            coEvery { projectExternalDataSource.getProjects(mockk()) } returns listOf(projectDto)

            assertDoesNotThrow {
                repository.deleteProject(project.id.toString())
            }
        }
    }

    @Test
    fun `editProject should edit project successfully when data source do it successfully`() {
        runTest {
            every { projectManager.editProject(projectDto) } returns listOf(projectDto)
            coEvery { projectExternalDataSource.getProjects(mockk()) } returns listOf(projectDto)

            assertDoesNotThrow {
                repository.editProject(project)

            }
        }
    }


    @Test
    fun `getProjectLogsById should return list of logs when it exists in projects list`() {
        runTest {
            coEvery { remoteProjectDataSource.getProjects(mockk()) } returns listOf(projectDto)
            every { projectManager.getProjects() } returns listOf(projectDto)

            assertDoesNotThrow {
                repository.getProjectLogsById(projectDto.id)
            }
        }
    }


    @Test
    fun `getProjectLogsById throw ProjectNotFoundException when project not exists in projects list`() {
        runTest {
            every { projectManager.getProjects() } returns listOf(projectDto)

            assertThrows<PlanMateExceptions> {
                repository.getProjectById(projectDto.id)
            }
        }
    }

    @Test
    fun `getProjectById should return the correct project when ID matches`() {
        runTest {
            val expectedProject = createProject(id = projectDto.id)
            coEvery { projectExternalDataSource.getProjectById(projectDto.id) } returns expectedProject

            val result = repository.getProjectById(projectDto.id)

            assertThat(expectedProject).isEqualTo(result.toDto())
        }
    }

    @Test
    fun `getProjectById should throw exception when ID does not match`() {
        runTest {
            every { projectManager.getProjects() } throws ProjectExceptions.ProjectNotFoundException()
            assertThrows<PlanMateExceptions> { repository.getProjectById(projectDto.id) }
        }
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
        id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
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
}