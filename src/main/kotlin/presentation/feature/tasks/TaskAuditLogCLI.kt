package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import domain.utlis.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskAuditLogCLI(
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val getTaskLogsByIdUseCase: GetTaskLogsUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        printer.printMessage("=== Task Audit Log ===")
        try {
            val projectId = reader.readInput("Enter Project ID:")
            val taskId = reader.readInput("Enter Task ID to view audit logs: ")
            val logs = getTaskLogsByIdUseCase(projectId, taskId)
            if (logs.isEmpty()) {
                printer.printMessage("No audit logs found for this task id : $taskId\n")
            } else {
                printer.printMessage("Audit logs for task id: $taskId\n")
                logs.forEach { log ->
                    printer.printMessage("- $log\n")
                }
            }
        } catch (exception: TaskExceptions.NoLogsFoundException) {
            printer.printError(errorMessage = "Failed to fetch audit logs : ${exception.message}\n")
        }
    }
}
