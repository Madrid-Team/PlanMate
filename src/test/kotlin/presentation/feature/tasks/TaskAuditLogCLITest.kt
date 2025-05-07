package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import domain.utlis.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.madrid.presentation.feature.tasks.TaskAuditLogCLI
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class TaskAuditLogCLITest {
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var useCase: GetTaskLogsUseCase
    private lateinit var cli: TaskAuditLogCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        reader = mockk()
        printer = mockk(relaxed = true)
        useCase = mockk()
        cli = TaskAuditLogCLI(reader, printer, useCase)
        testScope = TestScope()
    }

    @Test
    fun `show should print audit logs when logs exist`() {
        val taskId = "task-123"
        val logs = listOf("Log A", "Log B")

        every { reader.readInput(any()) } returns taskId
        coEvery { useCase.getTaskLogs("1",taskId) } returns logs

        cli.show()

        verify { printer.printMessage("Audit logs for task id: $taskId\n") }
        verify { printer.printMessage("- Log A\n") }
        verify { printer.printMessage("- Log B\n") }
    }

    @Test
    fun `show should print no logs message when logs are empty`() {
      testScope.launch {
          val taskId = "task-empty"

          every { reader.readInput(any()) } returns taskId
          coEvery { useCase.getTaskLogs("1",taskId) } returns emptyList()

          cli.show()

          verify { printer.printMessage("No audit logs found for this task id : $taskId\n") }
      }
    }

    @Test
    fun `should print message when no logs are found`() {
       testScope.launch {
           val taskId = "task-123"

           every { reader.readInput(any()) } returns taskId
           coEvery { useCase.getTaskLogs("",taskId) } returns emptyList()

           cli.show()

           verifySequence {
               printer.printMessage("=== Task Audit Log ===")
               reader.readInput("Enter Task ID to view audit logs: ")
               printer.printMessage("No audit logs found for this task id : $taskId\n")
           }
       }
    }

    @Test
    fun `should print error message when exception is thrown`() {
       testScope.launch {
           val taskId = "task-123"
           val errorMessage = "Task not found"

           every { reader.readInput(any()) } returns taskId
           coEvery { useCase.getTaskLogs("",taskId) } throws TaskExceptions.NoLogsFoundException(errorMessage)

           cli.show()

           verifySequence {
               printer.printMessage("=== Task Audit Log ===")
               reader.readInput("Enter Task ID to view audit logs: ")
               printer.printError(errorMessage = "Failed to fetch audit logs : ${errorMessage}\n")

           }
       }
    }
}