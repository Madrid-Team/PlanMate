package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import domain.validation.ValidateProjectName
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var validateProjectName: ValidateProjectName
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        validateProjectName = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository, validateProjectName)
    }

    @Test
    fun `should create project when all validations pass`() = runTest {
        //Given
        val project = createProject(
            name = "Test Project",
            description = "project description",
            createdBy = "user2",
            projectState = "Todo",
            taskStates = listOf("Todo", "In progress"),
            projectStates = listOf("Testing", "Todo"),
        )

        every { validateProjectName(any()) } returns Unit
        coEvery { projectRepository.createProject(project) } returns Unit

        //When & Then
        assertDoesNotThrow {
            createProjectUseCase.execute(project)
        }
        coVerify { projectRepository.createProject(project) }
    }

    @Test
    fun `should throw exception when project name validation fails`() = runTest {
        //Given
        val project = createProject(
            name = "Invalid Name",
        )
        every { validateProjectName(project) } throws ProjectExceptions.ProjectNameInvalidException()

        //When & Then
        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            createProjectUseCase.execute(project)
        }
    }

    @Test
    fun `should throw exception when description is empty`() = runTest {
        //Given
        val project = createProject(
            name = "Test Project",
            description = "",
        )
        every { validateProjectName(project) } returns Unit

        //When & Then
        assertThrows<PlanMateExceptions> {
            createProjectUseCase.execute(project)
        }
    }

    @Test
    fun `should throw exception when project states list is empty`() = runTest {
        //Given
        val project = createProject(
            name = "Test Project",
            description = "Description",
            projectStates = emptyList()
        )
        every { validateProjectName(project) } returns Unit

        //When & Then
        assertThrows<PlanMateExceptions> {
            createProjectUseCase.execute(project)
        }
    }

    @Test
    fun `should throw exception when task states list is empty`() = runTest {
        //Given
        val project = createProject(
            name = "Test Project",
            description = "Description",
            projectStates = listOf("Active"),
            taskStates = emptyList()
        )
        every { validateProjectName(project) } returns Unit

        //When & Then
        assertThrows<PlanMateExceptions> {
            createProjectUseCase.execute(project)
        }
    }
}