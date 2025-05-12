package presentation.feature.tasks

import domain.usecases.task.DisplayAllTasksUseCase
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class TaskCLI(
    private val createTaskCLI: CreateTaskCLI,
    private val editTaskCLI: EditTaskCLI,
    private val deleteTaskCLI: DeleteTaskCLI,
    private val taskAuditLogCLI: TaskAuditLogCLI,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader,
    private val displayAllTasksUseCase: DisplayAllTasksUseCase,

    ) {
    suspend fun show() {
        while (true) {
            outputPrinter.printMenuItems(
                listOf(
                    String.taskMenuHeader,
                    String.createTask,
                    String.editTask,
                    String.deleteTask,
                    String.displayAllTasksOption,
                    String.displayTaskLogs,
                    String.back
                )
            )
            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> createTaskCLI.show()
                String.selectionTwo -> editTaskCLI.show()
                String.selectionThree -> deleteTaskCLI.show()
                String.selectionFour -> getAllTasks()
                String.selectionFive -> taskAuditLogCLI.show()
                String.selectionZero -> return
                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }

    private suspend fun getAllTasks() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.displayAllTask)
        val projectId = inputReader.readInput(String.enterProjectId)

        try {
            val result = displayAllTasksUseCase.display(projectId)
            outputPrinter.printMessage(result)
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage(exception.message.toString())
        }
    }
}
