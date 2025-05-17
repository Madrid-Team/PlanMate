package presentation.feature.projects

import data.dto.authentication.UserDto
import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.usecases.project.CreateProjectUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val createProjectUseCase = mockk<CreateProjectUseCase>()
    private val currentUserProvider = mockk<CurrentUserProvider>()
    private val userMock = mockk<UserDto>()
    private lateinit var cli: CreateProjectCLI

    private val projectName = "project name"
    private val description = "description"
    private val taskStates = "TODO-IN_PROGRESS-DONE"
    private val projectStates = "ACTIVE-IN_PROGRESS-DONE"
    private val mockUUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val mockLogString = "User test-user CREATE PROJECT project name at 2025-05-13 12:00:00"

    @BeforeEach
    fun setUp() {
        every { userMock.username } returns "test-user"
        every { currentUserProvider.getCurrentUser() } returns userMock

        cli = CreateProjectCLI(inputReader, outputPrinter, createProjectUseCase, currentUserProvider)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns mockUUID

        mockkConstructor(AuditLog::class)
        every { anyConstructed<AuditLog>().toString() } returns mockLogString
    }

    private fun promptForProjectState(states: List<String>) =
        "Select project State:\n" + states.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n") + "\nEnter number: "

    @Test
    fun `should create project successfully`() = runTest {
        // Given
        val selectedStateIndex = "2"
        val expectedState = "IN_PROGRESS"
        val statesList = projectStates.split("-")

        every { inputReader.readInput("Enter project name: ") } returns projectName
        every { inputReader.readInput("Enter project description: ") } returns description
        every { inputReader.readInput("Enter task States separated by (-): ") } returns taskStates
        every { inputReader.readInput("Enter project States separated by (-): ") } returns projectStates
        every { inputReader.readInput(promptForProjectState(statesList)) } returns selectedStateIndex

        coEvery { createProjectUseCase.createProject(any()) } just runs

        // When
        cli.show()

        // Then
        coVerify {
            createProjectUseCase.createProject(match { project ->
                project.name == projectName &&
                        project.description == description &&
                        project.projectState == expectedState &&
                        project.taskStates == listOf("TODO", "IN_PROGRESS", "DONE") &&
                        project.projectStates == listOf("ACTIVE", "IN_PROGRESS", "DONE") &&
                        project.projectLogs.contains(mockLogString) &&
                        project.createdBy == "test-user" &&
                        project.id == mockUUID
            })
        }

        verifySequence {
            outputPrinter.printMessage("=== Create Project ===")
            outputPrinter.printMessage("Project created successfully")
        }
    }

    @Test
    fun `should handle invalid project state then accept valid`() = runTest {
        // Given
        val statesList = projectStates.split("-")
        every { inputReader.readInput("Enter project name: ") } returns projectName
        every { inputReader.readInput("Enter project description: ") } returns description
        every { inputReader.readInput("Enter task States separated by (-): ") } returns taskStates
        every { inputReader.readInput("Enter project States separated by (-): ") } returns projectStates
        every { inputReader.readInput(promptForProjectState(statesList)) } returnsMany listOf("5", "1")

        coEvery { createProjectUseCase.createProject(any()) } just runs

        // When
        cli.show()

        // Then
        coVerify {
            createProjectUseCase.createProject(match {
                it.projectState == "ACTIVE" &&
                        it.projectLogs.contains(mockLogString)
            })
        }
        verify { outputPrinter.printMessage("Project created successfully") }
    }

    @Test
    fun `should handle project creation failure`() = runTest {
        // Given
        val selectedStateIndex = "1"
        val statesList = projectStates.split("-")
        val exception = ProjectExceptions("Project already exists")

        every { inputReader.readInput("Enter project name: ") } returns projectName
        every { inputReader.readInput("Enter project description: ") } returns description
        every { inputReader.readInput("Enter task States separated by (-): ") } returns taskStates
        every { inputReader.readInput("Enter project States separated by (-): ") } returns projectStates
        every { inputReader.readInput(promptForProjectState(statesList)) } returns selectedStateIndex

        coEvery { createProjectUseCase.createProject(any()) } throws exception

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Failed to create project: ${exception.message}") }
    }
}
