package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.*
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

    @BeforeEach
    fun setup() {
        reader = mockk()
        printer = mockk(relaxed = true)
        useCase = mockk()
        cli = TaskAuditLogCLI(reader, printer, useCase)
    }

    @Test
    fun `show should print audit logs when logs exist`() {
        val taskId = "task-123"
        val logs = listOf("Log A", "Log B")

        every { reader.readInput(any()) } returns taskId
        every { useCase.getTaskLogs(taskId) } returns Result.success(logs)

        cli.show()

        verify { printer.printMessage("Audit logs for task id: $taskId\n") }
        verify { printer.printMessage("- Log A\n") }
        verify { printer.printMessage("- Log B\n") }
    }

    @Test
    fun `show should print no logs message when logs are empty`() {
        val taskId = "task-empty"

        every { reader.readInput(any()) } returns taskId
        every { useCase.getTaskLogs(taskId) } returns Result.success(emptyList())

        cli.show()

        verify { printer.printMessage("No audit logs found for this task id : $taskId\n") }
    }

    @Test
    fun `show should print error message when use case fails`() {
        val taskId = "task-fail"
        val exception = RuntimeException("Something failed")

        every { reader.readInput(any()) } returns taskId
        every { useCase.getTaskLogs(taskId) } returns Result.failure(exception)

        cli.show()

        verify { printer.printError(errorMessage = "Failed to fetch audit logs : Something failed\n") }
    }

}