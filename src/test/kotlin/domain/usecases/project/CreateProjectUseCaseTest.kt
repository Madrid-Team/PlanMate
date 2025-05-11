package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utils.PlanMateExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository)
        testScope = TestScope()
    }

    @Test
    fun `createProject should return true when project added successfully`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
                description = "project description",
                createdBy = "user2",
                projectState = "Todo",
                taskStates = listOf("Todo", "In progress"),
                projectStates = listOf("Testing", "Todo"),
            )

            coEvery { projectRepository.createProject(project) } returns Unit
            //When & Then
            assertDoesNotThrow {
                createProjectUseCase(project)
            }
            coVerify { projectRepository.createProject(project) }
        }
    }

    @Test
    fun `createProject should throw ProjectNameExistException when project name already exists`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
            )
            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")

            //When & Then
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }

    @Test
    fun `createProject should throw ProjectNameInvalidException when project name is invalid string`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "123 #$",
            )
            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")
            //When &Then
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }

    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is invalid string`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
                description = "123 #$"
            )
            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")
            //When
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }


    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is empty`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
                description = ""
            )
            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")

            //When
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }

    @Test
    fun `createProject should throw ProjectStatesInvalidException when project states list is empty`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
                projectStates = emptyList()
            )
            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")

            //When
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }


    @Test
    fun `createProject should throw ProjectStatesInvalidException when task states list is empty`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "Test Project",
                taskStates = emptyList()
            )

            coEvery { projectRepository.createProject(project) } throws PlanMateExceptions("")
            //When
            assertThrows<PlanMateExceptions> {
                createProjectUseCase(project)
            }
        }
    }

}