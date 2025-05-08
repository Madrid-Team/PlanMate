package presentation.feature.projects

import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.CreateProjectUseCase
import domain.utlis.PlanMateExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val createProjectUseCase = mockk<CreateProjectUseCase>()
    private val createLogUseCase = mockk<CreateLogUseCase>()

    private val userMock = mockk<User>()

    private lateinit var cli: CreateProjectCLI
    private lateinit var testScope: TestScope


    @BeforeEach
    fun setUp() {

        testScope = TestScope()
        mockkObject(CurrentUser)
        every { userMock.username } returns "test-user"
        every { CurrentUser.getCurrentUser() } returns userMock

        cli = CreateProjectCLI(inputReader, outputPrinter, createProjectUseCase, createLogUseCase)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("00000000-0000-0000-0000-000000000001")
    }

    @Test
    fun `should create project successfully when create project then return success`() {
        // Given
        every { inputReader.readInput("Enter project name: ") } returns "project name"
        every { inputReader.readInput("Enter project description: ") } returns "description"
        every { inputReader.readInput("Enter task States seperated by white space description: ") } returns "TODO IN_PROGRESS DONE"
        every { inputReader.readInput("Enter project States seperated by white space description: ") } returns "ACTIVE IN_PROGRESS DONE"
        every { inputReader.readInput(match { it.startsWith("Select project State:") }) } returns "1"

        val mockLogString = "User test-user CREATE PROJECT project name  at 2025-05-04 12:00:00"
        every {
            createLogUseCase.invoke(
                operationType = OperationType.CREATE,
                entityName = "project name",
                entityType = EntityType.PROJECT,
                username = "test-user"
            )
        } returns mockLogString

        val project = helperProject(
            id = "00000000-0000-0000-0000-000000000001",
            name = "project name",
            description = "description",
            projectLogs = listOf(mockLogString)
        )

        coEvery { createProjectUseCase.createProject(any()) } returns Unit

        // When
        cli.show()

        // Then
        coVerify {
            createProjectUseCase.createProject(match {
                it.name == project.name &&
                        it.description == project.description &&
                        it.projectState == "ACTIVE" &&
                        it.projectLogs.size == 1
            })
        }
        verify { outputPrinter.printMessage("Project created successfully") }
    }

    @Test
    fun `should show error message when creation fails`() {
        // Given
        every { inputReader.readInput("Enter project name: ") } returns "project name"
        every { inputReader.readInput("Enter project description: ") } returns "description"
        every { inputReader.readInput("Enter task States seperated by white space description: ") } returns "TODO IN_PROGRESS DONE"
        every { inputReader.readInput("Enter project States seperated by white space description: ") } returns "TODO IN_PROGRESS DONE"
        every { inputReader.readInput(match { it.startsWith("Select project State:") }) } returns "1"

        val mockLogString = "User test-user CREATE PROJECT project name  at 2025-05-04 12:00:00"
        every {
            createLogUseCase.invoke(
                operationType = OperationType.CREATE,
                entityName = "project name",
                entityType = EntityType.PROJECT,
                username = "test-user"
            )
        } returns mockLogString

        coEvery { createProjectUseCase.createProject(any()) } throws PlanMateExceptions("failed")

        // When
        cli.show()
        // Then
        coVerify { createProjectUseCase.createProject(any()) }
        coVerify { outputPrinter.printMessage("Failed to create project: failed") }
    }

    @Test
    fun `should handle invalid project state selection`() {
        // Given
        every { inputReader.readInput("Enter project name: ") } returns "project name"
        every { inputReader.readInput("Enter project description: ") } returns "description"
        every { inputReader.readInput("Enter task States seperated by white space description: ") } returns "TODO IN_PROGRESS DONE"
        every { inputReader.readInput("Enter project States seperated by white space description: ") } returns "TODO IN_PROGRESS DONE"

        // First input invalid, second valid
        every { inputReader.readInput(match { it.startsWith("Select project State:") }) } returnsMany listOf("5", "1")

        val mockLogString = "User test-user CREATE PROJECT project name  at 2025-05-04 12:00:00"
        every {
            createLogUseCase.invoke(any(), any(), any(), any())
        } returns mockLogString

        coEvery { createProjectUseCase.createProject(any()) } returns mockk()

        // When
        cli.show()

        // Then
        coVerify { createProjectUseCase.createProject(any()) }
        coVerify { outputPrinter.printMessage("Project created successfully") }
    }
}