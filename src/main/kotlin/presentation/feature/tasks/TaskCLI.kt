package presentation.feature.tasks

import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class TaskCLI(
    private val createTaskCLI: CreateTaskCLI,
    private val editTaskCLI: EditTaskCLI,
    private val deleteTaskCLI: DeleteTaskCLI,
    private val taskAuditLogCLI: TaskAuditLogCLI,
    private val taskView: TaskView,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {
    suspend fun show() {
        while (true) {
            outputPrinter.printMenuItems(
                listOf(
                    String.taskMenuHeader,
                    String.createTask,
                    String.editTask,
                    String.deleteTask,
                    String.displayTasks,
                    String.displayTaskLogs,
                    String.back
                )
            )
            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> createTaskCLI.show()
                String.selectionTwo -> editTaskCLI.show()
                String.selectionThree -> deleteTaskCLI.show()
                String.selectionFour -> taskView.show()
                String.selectionFive -> taskAuditLogCLI.show()
                String.selectionZero -> return
                else -> outputPrinter.printError( String.invalidOption)
            }
        }
    }
}
