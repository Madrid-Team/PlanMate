package presentation.feature.projects

import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.EditProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import kotlin.test.Test

class EditProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val editProjectUseCase = mockk<EditProjectUseCase>()
    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>()
    private val createLogUseCase = mockk<CreateLogUseCase>()
    private val userMock = mockk<User>()

    private lateinit var cli: EditProjectCLI

    @BeforeEach
    fun setUp() {
        mockkObject(CurrentUser)
        every { userMock.username } returns "test-user"
        every { CurrentUser.getCurrentUser() } returns userMock
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("00000000-0000-0000-0000-000000000001")
        cli = EditProjectCLI(inputReader, outputPrinter, editProjectUseCase, getProjectByIdUseCase, createLogUseCase)
    }

    @Test
    fun `should edit project name successfully`() {
        // Given
        val originalProject = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "original-name",
            description = "original-description",
            createdBy = "test-user",
            projectState = "TODO"
        )
        val mockLogString =
            "User test-user UPDATE PROJECT original-name name from original-name to new-name at 2025-05-04 12:00:00"


        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
        every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "4")
        every { inputReader.readInput("Enter the new name:") } returns "new-name"

        every { getProjectByIdUseCase.invoke("1") } returns originalProject
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = "original-name",
                entityType = EntityType.PROJECT,
                username = "test-user",
                fieldName = "name",
                oldValue = "original-name",
                newValue = "new-name",
                any()
            )
        } returns mockLogString

        every {
            editProjectUseCase.editProject(match {
                it.name == "new-name" &&
                        it.description == "original-description" &&
                        it.projectLogs.size == 1 &&
                        it.projectLogs.first() == mockLogString
            })
        } returns Unit

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project edited successfully.") }
        verify { editProjectUseCase.editProject(any()) }
    }

    @Test
    fun `should edit project description successfully`() {
        // Given
        val originalProject = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "original-name",
            description = "original-description",
            createdBy = "test-user",
            projectState = "TODO"
        )
        val mockLogString =
            "User test-user UPDATE PROJECT original-name description from original-description to new-description at 2025-05-04 12:00:00"

        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
        every { inputReader.readInput("Select an option:") } returnsMany listOf("2", "4")
        every { inputReader.readInput("Enter the new description:") } returns "new-description"

        every { getProjectByIdUseCase.invoke("1") } returns originalProject
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = "original-name",
                entityType = EntityType.PROJECT,
                username = "test-user",
                fieldName = "description",
                oldValue = "original-description",
                newValue = "new-description",
                any()
            )
        } returns mockLogString

        every {
            editProjectUseCase.editProject(match {
                it.name == "original-name" &&
                        it.description == "new-description" &&
                        it.projectLogs.size == 1 &&
                        it.projectLogs.first() == mockLogString
            })
        } returns Unit

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project edited successfully.") }
    }

    @Test
    fun `should edit project state successfully`() {
        // Given
        val originalProject = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "original-name",
            description = "original-description",
            createdBy = "test-user",
            projectState = "TODO",
            projectStates = listOf("TODO", "IN_PROGRESS", "DONE")
        )
        val mockLogString =
            "User test-user UPDATE PROJECT original-name project state from TODO to IN_PROGRESS at 2025-05-04 12:00:00"

        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
        every { inputReader.readInput("Select an option:") } returnsMany listOf("3", "4")
        every { inputReader.readInput(match { it.contains("Select project State:") }) } returns "2"

        every { getProjectByIdUseCase.invoke("1") } returns originalProject
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = "original-name",
                entityType = EntityType.PROJECT,
                username = "test-user",
                fieldName = "project state",
                oldValue = "TODO",
                newValue = "IN_PROGRESS",
                any()
            )
        } returns mockLogString

        every {
            editProjectUseCase.editProject(match {
                it.name == "original-name" &&
                        it.description == "original-description" &&
                        it.projectState == "IN_PROGRESS" &&
                        it.projectLogs.size == 1 &&
                        it.projectLogs.first() == mockLogString
            })
        } returns Unit

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project edited successfully.") }
    }

    @Test
    fun `should show error message when project not found`() {
        // Given
        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "999"
        every { getProjectByIdUseCase.invoke("999") } throws ProjectExceptions.ProjectNotFoundException()

        // When & Then
        cli.show()
        verify { outputPrinter.printMessage(ProjectExceptions.ProjectNotFoundException().message!!) }
        verify(exactly = 0) { editProjectUseCase.editProject(any()) }
    }

    @Test
    fun `should show error message when editing fails`() {
        // Given
        val originalProject = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "original-name",
            description = "original-description",
            createdBy = "test-user",
            projectState = "TODO"
        )
        val mockLogString =
            "User test-user UPDATE PROJECT original-name name from original-name to new-name at 2025-05-04 12:00:00"

        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
        every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "4")
        every { inputReader.readInput("Enter the new name:") } returns "new-name"

        every { getProjectByIdUseCase.invoke("1") } returns originalProject
        every { createLogUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any()) } returns mockLogString
        every { editProjectUseCase.editProject(any()) } throws PlanMateExceptions("Edit failed")

        // When & Then
        cli.show()

        verify { outputPrinter.printMessage(match { it.contains("Failed to edit project") }) }
    }

    @Test
    fun `should return early when no changes are made`() {
        // Given
        val originalProject = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "original-name",
            description = "original-description",
            createdBy = "test-user",
            projectState = "TODO"
        )

        every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
        every { inputReader.readInput("Select an option:") } returns "4"

        every { getProjectByIdUseCase.invoke("1") } returns originalProject

        // When
        cli.show()

        // Then
        verify(exactly = 0) { editProjectUseCase.editProject(any()) }
        verify(exactly = 0) { outputPrinter.printMessage("Project edited successfully.") }
    }
}