package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import domain.utils.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*
import kotlin.test.Test

class TaskAuditLogCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var getTaskLogsUseCase: GetTaskLogsUseCase
    private lateinit var cli: TaskAuditLogCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        getTaskLogsUseCase = mockk(relaxed = true)
        cli = TaskAuditLogCLI(inputReader, outputPrinter, getTaskLogsUseCase)
        testScope = TestScope()
    }

    @Test
    fun `should print audit logs when logs exist`() {
        testScope.runTest {
            // Given
            val taskId = "123"
            val logs = listOf("Log A", "Log B")
            every { inputReader.readInput(String.enterTaskIdToViewLogs) } returns taskId
            coEvery { getTaskLogsUseCase(taskId) } returns logs

            // When
            cli.show()

            // Then
            verifySequence {
                outputPrinter.printMessage(String.taskAuditLogHeader)
                inputReader.readInput(String.enterTaskIdToViewLogs)
                outputPrinter.printMessage(String.auditLogsForTaskId.format(taskId))
                outputPrinter.printMessage("- Log A\n")
                outputPrinter.printMessage("- Log B\n")
            }
        }
    }


    @Test
    fun `should print no logs message when logs are empty`() {
        testScope.runTest {
            // Given
            val taskId = "123"
            every { inputReader.readInput(String.enterTaskIdToViewLogs) } returns taskId
            coEvery { getTaskLogsUseCase(taskId) } returns emptyList()

            // When
            cli.show()

            // Then
            verifySequence {
                outputPrinter.printMessage(String.taskAuditLogHeader)
                inputReader.readInput(String.enterTaskIdToViewLogs)
                outputPrinter.printMessage(String.taskLogNotFound.format(taskId))
            }
        }
    }


    @Test
    fun `should print error message when exception is thrown`() {
        testScope.runTest {
            // Given
            val taskId = "123"
            val errorMessage = "Task not found"
            val exception = TaskExceptions.NoLogsFoundException(errorMessage)
            every { inputReader.readInput(String.enterTaskIdToViewLogs) } returns taskId
            coEvery { getTaskLogsUseCase(taskId) } throws exception

            // When
            cli.show()

            // Then
            verifySequence {
                outputPrinter.printMessage(String.taskAuditLogHeader)
                inputReader.readInput(String.enterTaskIdToViewLogs)
                outputPrinter.printError(String.auditLogException.format(exception))
            }
        }
    }
}