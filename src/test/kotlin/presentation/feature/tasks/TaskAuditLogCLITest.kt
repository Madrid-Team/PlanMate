package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import domain.utils.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class TaskAuditLogCLITest {
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var getTaskLogUseCase: GetTaskLogsUseCase
    private lateinit var cli: TaskAuditLogCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        reader = mockk()
        printer = mockk(relaxed = true)
        getTaskLogUseCase = mockk(relaxed = true)
        cli = TaskAuditLogCLI(reader, printer, getTaskLogUseCase)
        testScope = TestScope()
    }

    @Test
    fun `show should print audit logs when logs exist`() {
        testScope.runTest {
            every { reader.readInput("Enter Project ID:") } returns projectId
            every { reader.readInput("Enter Task ID to view audit logs: ") } returns taskId
            coEvery { getTaskLogUseCase.getTaskLogs(projectId = projectId, taskId = taskId) } returns logs

            cli.show()

            verifySequence {
                printer.printMessage("=== Task Audit Log ===")
                reader.readInput("Enter Project ID:")
                reader.readInput("Enter Task ID to view audit logs: ")
                printer.printMessage("Audit logs for task id: $taskId\n")
                printer.printMessage("- Log A\n")
                printer.printMessage("- Log B\n")
            }
        }
    }

    @Test
    fun `show should print no logs message when logs are empty`() {
        testScope.runTest {
            every { reader.readInput(any()) } returns taskId
            coEvery { getTaskLogUseCase.getTaskLogs("1", taskId) } returns emptyList()

            cli.show()

            verify { printer.printMessage("No audit logs found for this task id : $taskId\n") }
        }
    }

    @Test
    fun `should print message when no logs are found`() {
        testScope.runTest {
            every { reader.readInput(any()) } returns taskId
            coEvery { getTaskLogUseCase.getTaskLogs("", taskId) } returns emptyList()

            cli.show()

            verifySequence {
                printer.printMessage("=== Task Audit Log ===")
                reader.readInput("Enter Project ID:")
                reader.readInput("Enter Task ID to view audit logs: ")
                printer.printMessage("No audit logs found for this task id : $taskId\n")
            }
        }
    }

    @Test
    fun `should print error message when exception is thrown`() {
        testScope.runTest {
            every { reader.readInput("Enter Project ID:") } returns projectId
            every { reader.readInput("Enter Task ID to view audit logs: ") } returns taskId
            coEvery { getTaskLogUseCase.getTaskLogs(projectId, taskId) } throws TaskExceptions.NoLogsFoundException(errorMessage)

            cli.show()

            verifySequence {
                printer.printMessage("=== Task Audit Log ===")
                printer.printError("Failed to fetch audit logs : $errorMessage\n")
            }
        }
    }

    val projectId = "proj-123"
    private val taskId = "task-123"
    val logs = listOf("Log A", "Log B")
    private val errorMessage = "Task not found"
}