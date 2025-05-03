package presentation.feature

import domain.usecases.task.GetTaskLogsUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskAuditLogCLI(
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val getTaskLogsByIdUseCase: GetTaskLogsUseCase
) {
    fun show() {
        printer.printMessage("=== Task Audit Log ===")
        val taskId = reader.readInput("Enter Task ID to view audit logs: ")
        getTaskLogsByIdUseCase.getTaskLogs(taskId)
            .onSuccess { logs ->
                if (logs.isEmpty()) {
                    printer.printMessage("No audit logs found for this task id : $taskId\n")
                } else {
                    printer.printMessage("Audit logs for task id: $taskId\n")
                    logs.forEach { log ->
                        printer.printMessage("- $log\n")
                    }


                }
            }
            .onFailure { error ->
                printer.printError(errorMessage = "Failed to fetch audit logs : ${error.message}\n") }

    }
}
