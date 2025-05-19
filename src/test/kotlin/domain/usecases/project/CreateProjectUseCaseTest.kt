package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utils.ProjectDescriptionIsEmptyException
import domain.utils.ProjectNameInvalidException
import domain.utils.ProjectTaskStatesIsEmptyException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var projectValidator: ProjectValidator
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        projectValidator = mockk(relaxed = true)
        createProjectUseCase = CreateProjectUseCase(
            projectRepository,
            projectValidator
        )
        testScope = TestScope()
    }

    @Test
    fun `should create project successfully when data is valid`() {
        testScope.runTest {
            // Given
            val project = createProject(
                name = "Valid Project",
                description = "A valid description",
                projectStates = listOf("State1"),
                taskStates = listOf("TaskState1")
            )

            // When & Then
            assertDoesNotThrow {
                createProjectUseCase.createProject(project)
            }

            coVerify { projectValidator.validate(project) }
            coVerify { projectRepository.createProject(project) }
        }
    }

    @Test
    fun `should throw ProjectNameInvalidException when project name is invalid`() {
        testScope.runTest {
            // Given
            val project = createProject(
                name = "Invalid123",
                description = "Valid description",
                projectStates = listOf("State1"),
                taskStates = listOf("TaskState1")
            )

            coEvery { projectValidator.validate(project) } throws ProjectNameInvalidException()

            // When & Then
            assertThrows<ProjectNameInvalidException> {
                createProjectUseCase.createProject(project)
            }

            coVerify(exactly = 0) { projectRepository.createProject(any()) }
        }
    }

    @Test
    fun `should throw ProjectDescriptionIsEmptyException when project description is empty`() {
        testScope.runTest {
            // Given
            val project = createProject(
                name = "Valid Project",
                description = "",
                projectStates = listOf("State1"),
                taskStates = listOf("TaskState1")
            )

            coEvery { projectValidator.validate(project) } throws ProjectDescriptionIsEmptyException()

            // When & Then
            assertThrows<ProjectDescriptionIsEmptyException> {
                createProjectUseCase.createProject(project)
            }

            coVerify(exactly = 0) { projectRepository.createProject(any()) }
        }
    }


    @Test
    fun `should throw ProjectTaskStatesIsEmptyException when task states are empty`() {
        testScope.runTest {
            // Given
            val project = createProject(
                name = "Valid Project",
                description = "A valid description",
                projectStates = listOf("State1"),
                taskStates = listOf()
            )

            coEvery { projectValidator.validate(project) } throws ProjectTaskStatesIsEmptyException()

            // When & Then
            assertThrows<ProjectTaskStatesIsEmptyException> {
                createProjectUseCase.createProject(project)
            }

            coVerify(exactly = 0) { projectRepository.createProject(any()) }
        }
    }
}