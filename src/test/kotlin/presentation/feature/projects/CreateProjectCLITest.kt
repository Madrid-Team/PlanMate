package presentation.feature.projects

import domain.models.authentication.User
import domain.models.logs.AuditLog
import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.usecases.project.CreateProjectUseCase
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
     private val userMock = mockk<User>()
    private lateinit var cli: CreateProjectCLI

    @BeforeEach
    fun setUp() {
        mockkObject(CurrentUser)
        every { userMock.username } returns "test-user"
        every { CurrentUser.getCurrentUser() } returns userMock

        cli = CreateProjectCLI(inputReader, outputPrinter, createProjectUseCase)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("00000000-0000-0000-0000-000000000001")
    }

    @Test
    fun `should create project successfully when create project then return success`() {
        runTest {
            // Given
            every { inputReader.readInput("Enter project name: ") } returns "project name"
            every { inputReader.readInput("Enter project description: ") } returns "description"
            every { inputReader.readInput("Enter task States separated by white space description: ") } returns "TODO IN_PROGRESS DONE"
            every { inputReader.readInput("Enter project States separated by white space description: ") } returns "ACTIVE IN_PROGRESS DONE"
            every { inputReader.readInput(match { it.startsWith("Select project State:") }) } returns "1"

            val mockLogString = "User test-user CREATE PROJECT project name  at 2025-05-04 12:00:00"
            every {
                AuditLog(
                    operationType = OperationType.CREATE,
                    entityName = "project name",
                    entityType = EntityType.PROJECT,
                    username = "test-user"
                ).toString()
            } returns mockLogString

            val project = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "project name",
                description = "description",
                projectLogs = listOf(mockLogString)
            )

            coEvery { createProjectUseCase.execute(any()) } returns Unit

            // When
            cli.show()

            // Then
            coVerify {
                createProjectUseCase.execute(match {
                    it.name == project.name &&
                            it.description == project.description &&
                            it.projectState == "ACTIVE" &&
                            it.projectLogs.size == 1
                })
            }
            verify { outputPrinter.printMessage("Project created successfully") }
        }
    }

    @Test
    fun `should handle invalid project state selection`() {
        runTest {
            // Given
            every { inputReader.readInput("Enter project name: ") } returns "project name"
            every { inputReader.readInput("Enter project description: ") } returns "description"
            every { inputReader.readInput("Enter task States separated by white space description: ") } returns "TODO IN_PROGRESS DONE"
            every { inputReader.readInput("Enter project States separated by white space description: ") } returns "TODO IN_PROGRESS DONE"

            // First input invalid, second valid
            every { inputReader.readInput(match { it.startsWith("Select project State:") }) } returnsMany listOf(
                "5",
                "1"
            )

            val mockLogString = "User test-user CREATE PROJECT project name  at 2025-05-04 12:00:00"
            every {
               AuditLog(any(), any(), any(), any()).toString()
            } returns mockLogString

            coEvery { createProjectUseCase.execute(any()) } returns mockk()

            // When
            cli.show()

            // Then
            coVerify { createProjectUseCase.execute(any()) }
            coVerify { outputPrinter.printMessage("Project created successfully") }
        }
    }
}