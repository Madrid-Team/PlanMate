package presentation.feature.tasks

import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.usecases.logs.AddAuditLogUseCase
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetTaskByIdUseCase
import domain.utils.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.deleteTaskHeader)
        outputPrinter.printMessage(String.enterTaskIdToDelete)
        val taskId = inputReader.readInput()
        val task = getTaskByIdUseCase.getTaskById(taskId)
        try {
            deleteTaskUseCase.deleteTaskByTaskId(taskId)
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.DELETE,
                    entityName = task.title,
                    entityType = EntityType.TASK,
                    entityId = task.id.toString(),
                    projectId = task.projectId,
                    username = currentUserProvider.getCurrentUser().username,
                )
            )
            outputPrinter.printMessage(String.deleteTaskSuccess)
        } catch (exception: TaskExceptions) {
            outputPrinter.printError(String.deleteTaskException.format(exception))
        }
    }
}