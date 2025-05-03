package presentation.feature.tasks

import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskCLI(
    private val createTaskCLI: CreateTaskCLI,
    private val editTaskCLI: EditTaskCLI,
    private val deleteTaskCLI: DeleteTaskCLI,
    private val taskView: TaskView,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {
    fun show() {
        while (true) {
            outputPrinter.printMessage("\n=== Task Menu ===")
            outputPrinter.printMessage("1. Create Task")
            outputPrinter.printMessage("2. Edit Task")
            outputPrinter.printMessage("3. Delete Task")
            outputPrinter.printMessage("4. Display all tasks within a specific project")
            outputPrinter.printMessage("0. Back")

            when (inputReader.readInput("Select an option: ")) {
                "1" -> createTaskCLI.show()
                "2" -> editTaskCLI.show()
                "3" -> deleteTaskCLI.show()
                "4" -> taskView.show()
                "0" -> return
                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }
}
