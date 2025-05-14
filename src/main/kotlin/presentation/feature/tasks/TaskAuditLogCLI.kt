package presentation.feature.tasks

import domain.usecases.task.GetTaskLogsUseCase
import domain.utils.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class TaskAuditLogCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val getTaskLogsByIdUseCase: GetTaskLogsUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.taskAuditLogHeader)
        try {
            val taskId = inputReader.readInput(String.enterTaskIdToViewLogs)
            val logs = getTaskLogsByIdUseCase.getTaskLogsByTaskId(taskId)
            if (logs.isEmpty()) {
                outputPrinter.printMessage(String.taskLogNotFound.format(taskId))
            } else {
                outputPrinter.printMessage(String.auditLogsForTaskId.format(taskId))
                logs.forEach { log ->
                    outputPrinter.printMessage("- $log\n")
                }
            }
        } catch (exception: TaskExceptions) {
            outputPrinter.printError(String.auditLogException.format(exception))
        }
    }
}
